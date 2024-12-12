import time

from django.db.models.functions import ExtractYear, ExtractMonth

import requests
from selenium import webdriver
from selenium.common import TimeoutException
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

from datetime import timedelta, date, datetime
from dateutil.relativedelta import relativedelta
from bs4 import BeautifulSoup

from api.models import PlatformsRoomsInfo, PlatformsAuthInfo, StandardRoomsInfo, PreviousInfo, SupplyConsumption, Supply, AppUser

import pprint


######## 3 main functions start
def retriever(app_user: AppUser, start_date: date, end_date: date):
    """
    data retrieve mode collects data from web and collecting or updating data from and to database's previous_info table.
    collected data will be used for making prediction model or providing data to front for visualization of data.
    processes dates before today.
    :param app_user:
    :param start_date:
    :param end_date:
    :return: all booking data of given period's months
    """
    # end_date's month < this month condition adding maybe inserted at top here

    # previous data stored in db by monthly basis existing_year_month indicates them
    existing_year_month = (PreviousInfo.objects.filter(appUser=app_user)
             .annotate(year=ExtractYear('date'),month=ExtractMonth('date')).values_list('year', 'month').distinct())
    existing_year_month = list(existing_year_month)

    target_year_months = []
    result = {}
    curr = date(start_date.year, start_date.month, 1) # first day of each month
    # retrieved from db below
    while curr <= end_date:
        if (curr.year, curr.month) in existing_year_month:
            result.update(retrieve_info_from_db(app_user, curr, (curr + relativedelta(months=1)) - relativedelta(days=1))) # retrieve data from db
        else:
            target_year_months.append((curr.year, curr.month)) # store months that should be from retrieved from crawling
        curr = curr + relativedelta(months=1)
    # retrieved from web below
    if not target_year_months:
        # do not move into crawler if empty
        return result
    info_yapen, info_yogei = crawler(app_user, None, None, target_year_months)
    for target_year_month in target_year_months:
        st = date(target_year_month[0], target_year_month[1], 1)
        ed = date(target_year_month[0], target_year_month[1], 1) + relativedelta(months=1) - relativedelta(days=1)
        result.update(retrieve_info_from_bs(app_user, st, ed, info_yapen, info_yogei))

    return result

def detector(app_user: AppUser, start_date: date, end_date: date):
    """
    detector mode used for checking rooms mismatch or overbooked.
    processes dates subsequent from current date.
    :param app_user:
    :param start_date:
    :param end_date:
    :return: all mismatch and overbooking data of given period
    """
    info_yapen, info_yogei = crawler(app_user, start_date, end_date, None)

    result = {}
    target_date = start_date
    standard_room_infos = StandardRoomsInfo.objects.filter(appUser=app_user).order_by('display_order')
    while target_date <= end_date:
        day_problems = []
        day_yapen = info_yapen.get(target_date)
        day_yogei = info_yogei.get(target_date)
        for standard_room_info in standard_room_infos:
            standard_room_name = standard_room_info.room_name
            platform_room_infos = PlatformsRoomsInfo.objects.filter(standard_room_info=standard_room_info)
            booked = 0
            for platform_room_info in platform_room_infos:
                yapen_rn = platform_room_info.yapen_room_name
                yogei_rn = platform_room_info.yogei_room_name
                if yapen_rn:
                    if day_yapen.get(yapen_rn) == 2:
                        booked += 1
                if yogei_rn:
                    if day_yogei.get(yogei_rn) == 2:
                        booked += 1
                if yapen_rn and yogei_rn:
                    if day_yapen.get(yapen_rn) == 2 and day_yogei.get(yogei_rn) == 2:
                        # result.append({'date': target_date.strftime('%Y-%m-%d'),
                        #                'problem': 'mismatch',
                        #                'room_type': standard_room_name})
                        day_problems.append({'problem': 'mismatch',
                                       'room_type': standard_room_name})
                        print('mismatch\t', target_date, '\t', standard_room_name)

            if booked > standard_room_info.room_quantity:
                # result.append({'date': target_date.strftime('%Y-%m-%d'), 'problem': 'overbooked', 'room_type': standard_room_name})
                day_problems.append({'problem': 'overbooked',
                                     'room_type': standard_room_name})
                print('overbooked\t', target_date, '\t', standard_room_name)

        if day_problems:
            result.update({target_date.strftime('%Y-%m-%d'): day_problems})

        target_date += timedelta(days=1)

    if len(result) == 0:
        return {'message': 'no problem'}

    return result

    # num_platforms = 2
    # num_standard = num_platforms-1
    # target_date = start_date
    # platform_room_info = PlatformRoomInfoModel.objects.filter(user=user).order_by('display_order')
    # room_names = platform_room_info.values_list('default_room_name', flat=True)
    #
    # result = []
    # while target_date <= end_date:
    #     day_yapen = info_yapen.get(target_date)
    #     day_yogei = info_yogei.get(target_date)
    #     for room_name in room_names:
    #         platform_room_info_instance = platform_room_info.get(default_room_name=room_name)
    #         reason_var = 0
    #         yapen_rn = platform_room_info_instance.yapen_room_name
    #         yogei_rn = platform_room_info_instance.yogei_room_name
    #
    #         #################################################################################################
    #         # value of closed = num of platforms - 1
    #         # original status values
    #         # 0: onsale, 1: closed, 2: sold
    #
    #         #               platform_1      platform_2      platform_3      platform_4      total
    #         # room_type1    -1(closed)      -1(closed)      2(sold)                         0(==0, ok)
    #         # room_type2    0(open)         0(open)         0(open)                         0(==0, ok)
    #         # room_type3    2(sold)         -1(closed)      0(open)                         1(>0, mismatch)
    #         # room_type3    2(sold)         0(open)         0(open)                         2(>0, mismatch)
    #         # room_type3    2(sold)         2(sold)         -1(closed)                      3(>0, overbooked)
    #         # room_type4    3(sold)         -1(closed)      -1(closed)      -1(closed)      0(==0, ok)
    #         # room_type5    3(sold)         3(sold)         -1(closed)      -1(closed)      4(>0, overbooked)
    #         ##################################################################################################
    #
    #         if yapen_rn:
    #             if day_yapen.get(yapen_rn) == 1:
    #                 reason_var -= 1
    #             elif day_yapen.get(yapen_rn) == 2:
    #                 reason_var += num_standard
    #         if yogei_rn:
    #             if day_yogei.get(yogei_rn) == 1:
    #                 reason_var -= 1
    #             elif day_yogei.get(yogei_rn) == 2:
    #                 reason_var += num_standard
    #
    #         if reason_var >= num_platforms:
    #             result.append({'date': target_date.strftime('%Y-%m-%d'), 'problem': 'overbooked', 'room_type': room_name})
    #             # print('overbooked\t', target_date, '\t', room_name)
    #         elif reason_var > 0:
    #             result.append({'date': target_date.strftime('%Y-%m-%d'), 'problem': 'mismatch', 'room_type': room_name})
    #             # print('mismatch\t', target_date, '\t', room_name)
    #
    #     target_date += timedelta(days=1)
    #
    # return result

def supply_warner(app_user: AppUser, start_date: date, end_date: date):
    """
    supply warn mode used for checking evident shortage of supplies.
    processes dates subsequent from current date.
    :param app_user:
    :param start_date:
    :param end_date:
    :return: all insufficient supplies and its insufficiency
    """
    info_yapen, info_yogei = crawler(app_user, start_date, end_date, None)

    target_date = start_date
    standard_room_infos = StandardRoomsInfo.objects.filter(appUser=app_user).order_by('display_order')
    supplys = Supply.objects.filter(appUser=app_user)
    nec_sup_cnt = {}
    for supply in supplys:
        nec_sup_cnt.update({supply: 0})
    while target_date <= end_date:
        day_yapen = info_yapen.get(target_date)
        day_yogei = info_yogei.get(target_date)
        day = {}
        for standard_room_info in standard_room_infos:
            standard_room_name = standard_room_info.room_name
            booked = 0
            platform_room_infos = PlatformsRoomsInfo.objects.filter(standard_room_info=standard_room_info)
            for platform_room_info in platform_room_infos:
                yapen_rn = platform_room_info.yapen_room_name
                yogei_rn = platform_room_info.yogei_room_name
                if day_yapen.get(yapen_rn) == 2:
                    booked += 1
                if day_yogei.get(yogei_rn) == 2:
                    booked += 1

            supply_consumptions = SupplyConsumption.objects.filter(standard_room_info=standard_room_info)
            for supply_consumption in supply_consumptions:
                num = nec_sup_cnt[supply_consumption.supply]
                nec_sup_cnt[supply_consumption.supply] = num + supply_consumption.consumption

        target_date += timedelta(days=1)

    result = {}
    for supply in supplys:
        if nec_sup_cnt[supply] > supply.current_quantity:
            result.update({supply.name: nec_sup_cnt[supply] - supply.current_quantity})

    return result

######## end

############################################## crawling functions below ##############################################

def wait_until_page_fully_loaded(driver, month, timeout=30):
    """
    wait until page fully loaded
    :param driver:
    :param month:
    :param timeout:
    :return: True if page fully loaded
    """
    try:
        WebDriverWait(driver, 5).until(lambda d: d.execute_script("return document.readyState === 'complete'"))
        # WebDriverWait(driver, timeout).until(lambda d: d.execute_script('return window.isPageLoaded') == 'true')
        WebDriverWait(driver, 5).until(lambda d: d.execute_script('return window.jQuery != undefined && jQuery.active == 0'))
        # css-11r9wyy e1n6gliz1 - elements that would exist for data prior today
        # css-17oiwyz e1n6gliz1 - elements that would exist for data from and after today
        if month <= datetime.now().month:
            WebDriverWait(driver, timeout).until(expected_conditions.visibility_of_element_located((By.CLASS_NAME, "css-17oiwyz")))
        else:
            WebDriverWait(driver, timeout).until(expected_conditions.visibility_of_element_located((By.CLASS_NAME, "css-11r9wyy")))
        return True
    except TimeoutException:
        return False

def crawler(app_user: AppUser, start_date: date, end_date: date, year_months: list[tuple[int,int]]):
    """
    crawl data from websites using selenium. implicitly use bot_yapen, bot_yogei functions.
    :param app_user:
    :param start_date:
    :param end_date:
    :param year_months: if this parameter - list of (year,month) tuples - is given, function ignores start_date, end_date and uses this parameter
    :return:
    """
    chrome_options = Options()
    chrome_options.add_argument("--no-sandbox")  # 리눅스 환경에서 필요할 수 있음
    chrome_options.add_argument("--disable-dev-shm-usage")  # 메모리 부족 문제 해결
    chrome_options.add_argument("--window-size=1920,1080")  # 적절한 화면 크기 설정
    chrome_options.add_argument("--disable-notifications")

    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()), options=chrome_options)
    # implicitly waits for existence of every target element
    driver.implicitly_wait(15)

    platform_info = PlatformsAuthInfo.objects.get(appUser=app_user)
    info_yapen = bot_yapen(driver, start_date, end_date, year_months, platform_info)
    info_yogei = bot_yogei(driver, start_date, end_date, year_months, platform_info)
    driver.quit()
    return info_yapen, info_yogei

def generate_url_yapen(year, month):
    # eg: https://ceo.yapen.co.kr/rev/calendar?setDate=2024-10
    param = f"setDate={year}-{month}"
    url = f"https://ceo.yapen.co.kr/rev/calendar?{param}"
    return url

def get_one_month_yapen(session, target_date: date):
    year = target_date.year
    month = target_date.month
    url = generate_url_yapen(year, month)

    response = session.get(url)
    soup = BeautifulSoup(response.text, 'lxml')
    days = soup.find_all('table', attrs={"class": "roomListsTbl"})

    month_info = {}
    for day in days:
        date_str = day.parent.find('div', attrs={"class": "dayTitle"}).find('b').string
        date_obj = date(year=year, month=month, day=int(date_str))

        siblings = day.find_all('td', attrs={"class": "checkLayer"})
        day_info = {}
        for sibling in siblings:
#             ['\n',
#             < td class ="checkLayer" > < / td >,  <-- this is sibling
#             '\n',
#             < td >
#               < span class ="xIcon" > ／ < / span >
#               < label class ="roomMemoView" data-revdate="2024-10-01" id="roomMemo_205176377" style="" >
#                   커플스탠다드 - 1(강조망X)
#                   < div class ="resSubInfoWrap"> ... many sub elements ... < / div >
#               < / label >
#               < div class ="price" > 원 < / div >
#             < / td >,
#             '\n']
            room = sibling.parent.contents[3]
            stat_icon = room.find('span').get_text()
            room_name = room.find('label', recursive=False).contents[0].get_text(strip=True)

            status = 1
            if stat_icon == '가':
                status = 0
            elif stat_icon == '완':
                status = 2
            day_info[room_name] = status

        month_info[date_obj] = day_info

    return month_info

def bot_yapen(driver, start_date: date, end_date: date, year_months: list[tuple[int,int]], platform_info: PlatformsAuthInfo):
    ## login
    driver.get('https://ceo.yapen.co.kr')
    id_input = driver.find_element(By.ID, "ceoID")
    pass_input = driver.find_element(By.ID, "ceoPW")
    login_button = driver.find_element(By.CLASS_NAME, "yapen-loginBtn")
    id_input.send_keys(platform_info.yapen_id)
    pass_input.send_keys(platform_info.yapen_pass)
    login_button.click()
    # explicitly wait for user specific graph to be present
    WebDriverWait(driver, 15).until(expected_conditions.presence_of_element_located((By.ID, "channelChart")))
    ## hand over info to session
    session = requests.Session()
    cookies = driver.get_cookies()
    for cookie in cookies:
        session.cookies.set(cookie["name"], cookie["value"])
    user_agent = driver.execute_script("return navigator.userAgent")
    session.headers['User-Agent'] = user_agent

    info = {}
    if year_months:
        for year_month in year_months:
            target_date = date(year_month[0], year_month[1], 1)
            info.update(get_one_month_yapen(session, target_date))
    else:
        target_date = start_date.replace(day=1)
        while target_date <= end_date:
            info.update(get_one_month_yapen(session, target_date))
            target_date += relativedelta(months=1)

    return info

def generate_url_yogei(year, month):
    # eg: https://partner.goodchoice.kr/sales/status?date=2024.11
    param = f"date={year}.{month}"
    url = f"https://partner.goodchoice.kr/sales/status?{param}"
    return url

def get_one_month_yogei(driver, target_date: date):
    year = target_date.year
    month = target_date.month
    url = generate_url_yogei(year, month)
    driver.get(url)

    # wait until dynamic page elements loaded
    wait_until_page_fully_loaded(driver, 15)
    if not wait_until_page_fully_loaded(driver=driver, month=month):
        print("Page not fully loaded within given time")
        return {}
    if month == datetime.now().month:
        hide_previous_btn = WebDriverWait(driver, 15).until(expected_conditions.presence_of_element_located((By.CLASS_NAME, "css-qdf8m4"))).find_element(By.TAG_NAME, 'button')
        hide_previous_btn.click()
        time.sleep(1)

    html = driver.page_source
    soup = BeautifulSoup(html, 'lxml')
    days = soup.find_all('td', {'class': 'css-m6bnnw eg699vq6'})
    month_info = {}
    for day in days:
        day_info = {}
        date_str = day.find('span', {'class': 'eg699vq3'}).string
        date_obj = date(year=year, month=month, day=int(date_str))
        rooms = day.find_all('div', {'class': 'css-17oiwyz e1n6gliz1'}) # shadowed
        if not rooms:
            rooms = day.find_all('div', {'class': 'css-11r9wyy e1n6gliz1'}) # not shadowed

        for room in rooms:
            room_name = room.find('p', {'class': 'css-1jmeae7 e18j92hw0'}).string
            # stat_parts = room.find('div', {'class': 'css-1k71jzs e1n6gliz0'}).findAll('span')
            # reserved = stat_parts[0].string # 예약 0・
            # left = stat_parts[1].string # 잔여 1
            stats = room.find('div', {'class': 'css-12srjfc e18j92hw1'})
            stat = stats.contents[0].get_text()

            # 판매중   마감    판매완료
            # 0         1         2
            status = 1
            if stat == '판매중':
                status = 0
            elif stat == '판매완료':
                status = 2
            day_info[room_name] = status

        month_info[date_obj] = day_info

    return month_info

def bot_yogei(driver, start_date: date, end_date: date, year_months: list[tuple[int,int]], platform_info: PlatformsAuthInfo):
    ## login
    driver.get('https://partner.goodchoice.kr/')
    id_input = driver.find_element(By.NAME, "userId")
    pass_input = driver.find_element(By.NAME, "password")
    login_button = driver.find_element(By.CLASS_NAME, "e1xz4bhj2")
    id_input.send_keys(platform_info.yogei_id)
    pass_input.send_keys(platform_info.yogei_pass)
    login_button.click()
    ## disable popups - not necessary
    # driver.execute_script("""
    #     var modal = document.getElementById('modal-root');
    #     if (modal) {
    #         modal.remove();
    #     }
    # """)

    # explicitly wait for user specific element(name of accommodation) to be present
    WebDriverWait(driver, 15).until(expected_conditions.presence_of_element_located((By.CLASS_NAME, "css-li2428")))

    info = {}
    if year_months:
        for year_month in year_months:
            target_date = date(year_month[0], year_month[1], 1)
            tmp = get_one_month_yogei(driver, target_date)
            info.update(tmp)
    else:
        target_date = start_date.replace(day=1)
        while target_date <= end_date:
            info.update(get_one_month_yogei(driver, target_date))
            target_date += relativedelta(months=1)

    return info

############################################## retriever functions below ##############################################

def retrieve_info_from_bs(app_user: AppUser, start_date: date, end_date: date, info_yapen, info_yogei):
    """
    rearrange data arranged at bot_yapen and bot_yogei by extracting booked info only.
    update db's previous_info table.
    :param app_user:
    :param start_date:
    :param end_date:
    :param info_yapen:
    :param info_yogei:
    :return:
    """
    result = {}
    target_date = start_date
    standard_room_infos = StandardRoomsInfo.objects.filter(appUser=app_user).order_by('display_order')
    while target_date <= end_date:
        day_yapen = info_yapen.get(target_date)
        day_yogei = info_yogei.get(target_date)
        day = {}
        for standard_room_info in standard_room_infos:
            standard_room_name = standard_room_info.room_name
            yapen_booked = 0
            yogei_booked = 0
            platform_room_infos = PlatformsRoomsInfo.objects.filter(standard_room_info=standard_room_info)
            for platform_room_info in platform_room_infos:
                yapen_rn = platform_room_info.yapen_room_name
                yogei_rn = platform_room_info.yogei_room_name
                if day_yapen.get(yapen_rn) == 2:
                    yapen_booked += 1
                if day_yogei.get(yogei_rn) == 2:
                    yogei_booked += 1
            day.update({standard_room_name: {'yapen': yapen_booked, 'yogei': yogei_booked}})
            PreviousInfo.objects.create(appUser=app_user, standard_room_info=standard_room_info,
                                        date=target_date, yapen_booked=yapen_booked, yogei_booked=yogei_booked)

        result.update({target_date.strftime('%Y-%m-%d'): day})
        target_date += timedelta(days=1)

    return result

def retrieve_info_from_db(app_user: AppUser, start_date: date, end_date: date):
    """
    retrieve data from db.
    :param app_user:
    :param start_date:
    :param end_date:
    :return:
    """
    result = {}
    target_date = start_date
    standard_room_infos = StandardRoomsInfo.objects.filter(appUser=app_user).order_by('display_order')
    while target_date <= end_date:
        day = {}
        for standard_room_info in standard_room_infos:
            tmp = PreviousInfo.objects.get(appUser=app_user, standard_room_info=standard_room_info, date=target_date)
            day.update({standard_room_info.room_name: {'yapen': tmp.yapen_booked, 'yogei': tmp.yogei_booked}})

        result.update({target_date.strftime('%Y-%m-%d'): day})
        target_date += timedelta(days=1)

    return result


