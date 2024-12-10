import datetime
import pprint
from django.test import TestCase
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
from api.models import AppUser, PlatformsAuthInfo


# Create your tests here.
class ApiTest(TestCase):

    def setUp(self):
        self.appUser = AppUser.objects.get(username='test1')
        self.start_date = datetime.date(2024, 12, 10)
        self.end_date = datetime.date(2024, 12, 30)
        self.platform_info = PlatformsAuthInfo.objects.get(appUser=self.appUser)
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")  # 리눅스 환경에서 필요할 수 있음
        chrome_options.add_argument("--disable-dev-shm-usage")  # 메모리 부족 문제 해결
        chrome_options.add_argument("--window-size=1920,1080")  # 적절한 화면 크기 설정
        chrome_options.add_argument("--disable-notifications")
        driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()), options=chrome_options)
        self.driver = driver

    def test_yogei_bot(self):
        result = bot_yogei(self.driver, self.start_date, self.end_date, self.platform_info)
        pprint.pprint(result)

    def test_yapen_bot(self):
        result = bot_yapen(self.driver, self.start_date, self.end_date, self.platform_info)
        pprint.pprint(result)