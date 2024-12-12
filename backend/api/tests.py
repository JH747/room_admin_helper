from datetime import timedelta, date, datetime
import pprint

from django.db.models.functions import ExtractYear, ExtractMonth
from django.test import TestCase, TransactionTestCase
from selenium import webdriver
from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager

from dateutil.relativedelta import relativedelta
from bs4 import BeautifulSoup
from api.bot import bot_yogei, bot_yapen
from api.models import AppUser, PlatformsAuthInfo, PreviousInfo


# Create your tests here.
class ApiTest(TransactionTestCase):

    def setUp(self):
        self.username = 'test1'
        self.appUser = AppUser.objects.get(username='test1')
        self.start_date = date(2024, 12, 15)
        self.end_date = date(2024, 12, 30)
        self.platform_info = PlatformsAuthInfo.objects.get(appUser=self.appUser)
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")  # 리눅스 환경에서 필요할 수 있음
        chrome_options.add_argument("--disable-dev-shm-usage")  # 메모리 부족 문제 해결
        chrome_options.add_argument("--window-size=1920,1080")  # 적절한 화면 크기 설정
        chrome_options.add_argument("--disable-notifications")
        driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()), options=chrome_options)
        driver.implicitly_wait(10)
        self.driver = driver

    def test_yapen_bot(self):
        self.start_date = None
        self.end_date = None
        result = bot_yapen(self.driver, self.start_date, self.end_date, [(2024,10),(2024,11)], self.platform_info)
        pprint.pprint(result)

    def test_yogei_bot(self):
        self.start_date = None
        self.end_date = None
        result = bot_yogei(self.driver, self.start_date, self.end_date, [(2024,10),(2024,11)], self.platform_info)
        pprint.pprint(result)
        # find out way to wait until loading of page properly


    def test_db(self):
        dates = PreviousInfo.objects.filter(appUser=self.appUser).annotate(year=ExtractYear('date'), month=ExtractMonth('date')).values('year', 'month').distinct()
        print(list(dates))
        dates = PreviousInfo.objects.filter(appUser=self.appUser).annotate(year=ExtractYear('date'), month=ExtractMonth('date')).values_list('year', 'month').distinct()
        print(list(dates))

    def test_detector(self):
        self.start_date = date(2024, 12, 15)
        self.end_date = date(2024, 12, 30)
        response = self.client.get(f'/api/processes/?username={self.username}&start_date={self.start_date}&end_date={self.end_date}&mode=detect')
        pprint.pprint(response)

    def test_retriever(self):
        self.start_date = date(2024, 10, 15)
        self.end_date = date(2024, 10, 30)
        response = self.client.get(f'/api/processes/?username={self.username}&start_date={self.start_date}&end_date={self.end_date}&mode=retrieve')
        pprint.pprint(response.json())

    def test_supply_warn(self):
        response = self.client.get(f'/api/processes/?username={self.username}&start_date={self.start_date}&end_date={self.end_date}&mode=supply_warn')
        pprint.pprint(response)
