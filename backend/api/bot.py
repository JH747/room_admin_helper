import requests
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

from api.models import PlatformAuthInfoModel, PlatformRoomInfoModel
from datetime import datetime, timedelta
from dateutil.relativedelta import relativedelta
from bs4 import BeautifulSoup

def bot_integrated(user, start_date, end_date):
    chrome_options = Options()
    chrome_options.add_argument("--disable-notifications")
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()), options=chrome_options)
    # implicitly waits for existence of every target element
    driver.implicitly_wait(15)

    platform_info = PlatformAuthInfoModel.objects.get(user=user)

    info_yapen = bot_yapen(driver, start_date, end_date, platform_info)

    info_yogei = bot_yogei(driver, start_date, months, platform_info)

    driver.quit()

    num_platforms = 2
    num_standard = num_platforms-1
    target_date = start_date
    platform_room_info = PlatformRoomInfoModel.objects.filter(user=user).order_by('display_order')
    room_names = platform_room_info.values_list('default_room_name', flat=True)

    result = []
    while target_date <= end_date:
        day_yapen = info_yapen.get(target_date)
        day_yogei = info_yogei.get(target_date)
        for room_name in room_names:
            platform_room_info_instance = platform_room_info.get(default_room_name=room_name)
            reason_var = 0
            yapen_rn = platform_room_info_instance.yapen_room_name
            yogei_rn = platform_room_info_instance.yogei_room_name

            #################################################################################################
            # value of closed = num of platforms - 1
            # original status values
            # 0: onsale, 1: closed, 2: sold

            #               platform_1      platform_2      platform_3      platform_4      total
            # room_type1    -1(closed)      -1(closed)      2(sold)                         0(==0, ok)
            # room_type2    0(open)         0(open)         0(open)                         0(==0, ok)
            # room_type3    2(sold)         -1(closed)      0(open)                         1(>0, mismatch)
            # room_type3    2(sold)         0(open)         0(open)                         2(>0, mismatch)
            # room_type3    2(sold)         2(sold)         -1(closed)                      3(>0, overbooked)
            # room_type4    3(sold)         -1(closed)      -1(closed)      -1(closed)      0(==0, ok)
            # room_type5    3(sold)         3(sold)         -1(closed)      -1(closed)      4(>0, overbooked)
            ##################################################################################################

            if yapen_rn:
                if day_yapen.get(yapen_rn) == 1:
                    reason_var -= 1
                elif day_yapen.get(yapen_rn) == 2:
                    reason_var += num_standard
            if yogei_rn:
                if day_yogei.get(yogei_rn) == 1:
                    reason_var -= 1
                elif day_yogei.get(yogei_rn) == 2:
                    reason_var += num_standard

            if reason_var >= num_platforms:
                result.append({'date': target_date.strftime('%Y-%m-%d'), 'problem': 'overbooked', 'room_type': room_name})
                # print('overbooked\t', target_date, '\t', room_name)
            elif reason_var > 0:
                result.append({'date': target_date.strftime('%Y-%m-%d'), 'problem': 'mismatch', 'room_type': room_name})
                # print('mismatch\t', target_date, '\t', room_name)

        target_date += timedelta(days=1)

    return result

def generate_url_yapen(year, month):
    # eg: https://ceo.yapen.co.kr/rev/calendar?setDate=2024-10
    param = f"setDate={year}-{month}"
    url = f"https://ceo.yapen.co.kr/rev/calendar?{param}"
    return url

def get_one_month_yapen(session, target_date):
    year = target_date.year
    month = target_date.month
    url = generate_url_yapen(year, month)

    response = session.get(url)
    soup = BeautifulSoup(response.text, 'lxml')
    days = soup.find_all('table', attrs={"class": "roomListsTbl"})

    month_info = {}
    for day in days:
        date_str = day.parent.find('div', attrs={"class": "dayTitle"}).find('b').string
        date_obj = datetime(year=year, month=month, day=int(date_str))

        siblings = day.find_all('td', attrs={"class": "checkLayer"})
        day_info = {}
        for sibling in siblings:
#             ['\n',
#             < td class ="checkLayer" > < / td >,
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
#             rooms.append(tmp.parent.contents[0])
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

def bot_yapen(driver, start_date, end_date, platform_info):
    ## login
    driver.get('https://ceo.yapen.co.kr')
    id_input = driver.find_element(By.ID, "ceoID")
    pass_input = driver.find_element(By.ID, "ceoPW")
    login_button = driver.find_element(By.CLASS_NAME, "yapen-loginBtn")
    id_input.send_keys(platform_info.yapen_id)
    pass_input.send_keys(platform_info.yapen_pass)
    login_button.click()
    # explicitly wait for user specific graph to be visible
    WebDriverWait(driver, 15).until(expected_conditions.visibility_of_element_located((By.ID, "channelChart")))

    ## hand over info to session
    session = requests.Session()
    cookies = driver.get_cookies()
    for cookie in cookies:
        session.cookies.set(cookie["name"], cookie["value"])
    user_agent = driver.execute_script("return navigator.userAgent")
    session.headers['User-Agent'] = user_agent

    target_date = start_date.replace(day=1)

    info = {}

    while target_date <= end_date:
        info.update(get_one_month_yapen(session, target_date))
        target_date += relativedelta(months=1)

    return info

def generate_url_yogei(year, month):
    # eg: https://partner.goodchoice.kr/sales/product-start-stop?2024.10
    # eg: https://partner.goodchoice.kr/sales/status?date=2024.11
    param = f"date={year}.{month}"
    url = f"https://partner.goodchoice.kr/sales/status?{param}"
    return url

def get_one_month_yogei(driver, year, month):
    url = generate_url_yogei(year, month)
    driver.get(url)

    hide_former_btn = WebDriverWait(driver, 15).until(expected_conditions.visibility_of_element_located((By.CSS_SELECTOR, "button.css-1s7juq8.es5gqx46")))
    hide_former_btn.click()
    WebDriverWait(driver, 15).until(expected_conditions.presence_of_all_elements_located((By.CLASS_NAME, "css-m6bnnw")))
    # time.sleep(10)

    html = driver.page_source
    soup = BeautifulSoup(html, 'lxml')
    days = soup.findAll('td', {'class': 'css-m6bnnw eg699vq6'})

    month_info = {}
    for day in days:
        day_info = {}
        date_str = day.find('span', {'class': 'eg699vq3'}).string
        date_obj = datetime(year=year, month=month, day=int(date_str))
        rooms = day.findAll('div', {'class': 'css-11r9wyy e1n6gliz1'})
        if not rooms:
            continue
        for room in rooms:
            room_name = room.find('p', {'class': 'css-1jmeae7 e14u6bjd0'}).string
            # stat_parts = room.find('div', {'class': 'css-1k71jzs e1n6gliz0'}).findAll('span')
            # reserved = stat_parts[0].string # 예약 0・
            # left = stat_parts[1].string # 잔여 1
            stats = room.find('div', {'class': 'css-12srjfc e14u6bjd1'})
            stat = stats.contents[0].string

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

def bot_yogei(driver, curr_date, months, platform_info):
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
    # explicitly wait for user specific accommodation name to be visible
    WebDriverWait(driver, 15).until(expected_conditions.visibility_of_element_located((By.CLASS_NAME, "css-1cj511q")))

    target_year = curr_date.year
    target_month = curr_date.month

    info = {}
    for i in range(months):
        info.update(get_one_month_yogei(driver, target_year, target_month))
        target_month += 1

    return info