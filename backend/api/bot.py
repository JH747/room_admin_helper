import requests
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

import time
from datetime import datetime
from bs4 import BeautifulSoup

from api.models import PlatformInfoModel


def bot_integrated(months, user):
    chrome_options = Options()
    chrome_options.add_argument("--disable-notifications")
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()), options=chrome_options)
    # implicitly waits for existence of every target element
    driver.implicitly_wait(15)

    months = int(months)
    user_id = user.id
    platform_info = PlatformInfoModel.objects.get(user_id=user_id)

    info_yapen = bot_yapen(driver, months, platform_info)
    # print(info_yapen)
    info_yogei = bot_yogei(driver, months, platform_info)
    # print(info_yogei)
    driver.quit()


    return 'good result'



def generate_url_yapen(year, month):
    # eg: https://ceo.yapen.co.kr/rev/inventory?setDate=2024-10
    param = f"setDate={year}-{month}"
    url = f"https://ceo.yapen.co.kr/rev/inventory?{param}"
    return url

def get_one_month_yapen(session, year, month):
    url = generate_url_yapen(year, month)
    response = session.get(url)
    soup = BeautifulSoup(response.text, 'lxml')
    days = soup.findAll('table', attrs={"class": "roomListsTbl"})
    month_info = []
    for day in days:
        rooms = day.findAll("tr")
        date_obj = None
        day_info = []

        for room in rooms:
            stat_icon = room.find("span")
            room_name_or_date = room.find("label")

            if stat_icon is None:
                if room_name_or_date is None:
                    continue
                date_str = room_name_or_date['for'].split('_')[1]
                date_obj = datetime.strptime(date_str, '%Y-%m-%d')
            else:
                status = 2
                if stat_icon.string == '가':
                    status = 0
                elif stat_icon.string == '완':
                    status = 1
                day_info.append({room_name_or_date.string: status})

        month_info.append({date_obj: day_info})

    return month_info

def bot_yapen(driver, months, platform_info):
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

    current_date = datetime.now()
    target_year = current_date.year
    target_month = current_date.month

    info = []
    for i in range(months):
        target_month += 1
        info.extend(get_one_month_yapen(session, target_year, target_month))

    return info

def generate_url_yogei(year, month):
    # eg: https://partner.goodchoice.kr/sales/product-start-stop?2024.10
    param = f"date={year}.{month}"
    url = f"https://partner.goodchoice.kr/sales/product-start-stop?{param}"
    return url

def get_one_month_yogei(driver, year, month):
    url = generate_url_yogei(year, month)
    driver.get(url)

    WebDriverWait(driver, 15).until(expected_conditions.presence_of_all_elements_located((By.CLASS_NAME, "css-m6bnnw")))
    # time.sleep(10)

    html = driver.page_source
    soup = BeautifulSoup(html, 'lxml')
    days = soup.findAll('td', {'class': 'css-m6bnnw eg699vq6'})

    month_info = []
    for day in days:
        day_info = []
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

            # 판매중   판매완료    마감
            # 0       1         2
            status = 2
            if stat == '판매중':
                status = 0
            elif stat == '판매완료':
                status = 1
            day_info.append({room_name: status})

        month_info.append({date_obj: day_info})

    return month_info

def bot_yogei(driver, months, platform_info):
    ## login
    driver.get('https://partner.goodchoice.kr/')
    id_input = driver.find_element(By.NAME, "userId")
    pass_input = driver.find_element(By.NAME, "password")
    login_button = driver.find_element(By.CLASS_NAME, "e1xz4bhj2")
    id_input.send_keys(platform_info.yogei_id)
    pass_input.send_keys(platform_info.yogei_pass)
    login_button.click()
    # driver.execute_script("""
    #     var modal = document.getElementById('modal-root');
    #     if (modal) {
    #         modal.remove();
    #     }
    # """)
    # explicitly wait for user specific accommodation name to be visible
    WebDriverWait(driver, 15).until(expected_conditions.visibility_of_element_located((By.CLASS_NAME, "css-1cj511q")))

    current_date = datetime.now()
    target_year = current_date.year
    target_month = current_date.month

    info = []
    for i in range(months):
        target_month += 1
        part = get_one_month_yogei(driver, target_year, target_month)
        info.extend(part)

    return info