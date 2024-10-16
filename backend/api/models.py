from django.contrib.auth.models import User
from django.db import models

# Create your models here.
class PlatformInfoModel(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    yapen_id = models.CharField(max_length=50, blank=True, null=True)
    yapen_pass = models.CharField(max_length=50, blank=True, null=True)
    yogei_id = models.CharField(max_length=50, blank=True, null=True)
    yogei_pass = models.CharField(max_length=50, blank=True, null=True)
    naver_id = models.CharField(max_length=50, blank=True, null=True)
    naver_pass = models.CharField(max_length=50, blank=True, null=True)
    bnb_id = models.CharField(max_length=50, blank=True, null=True)
    bnb_pass = models.CharField(max_length=50, blank=True, null=True)