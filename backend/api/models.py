from django.db import models

class AppUser(models.Model):
    username = models.CharField
    password = models.CharField
    email = models.CharField

    class Meta:
        managed = False  # Django가 이 테이블을 관리하지 않도록 설정
        db_table = 'app_user'  # 기존 데이터베이스 테이블 이름

class PlatformsAuthInfo(models.Model):
    appUser = models.OneToOneField(AppUser, on_delete=models.CASCADE)
    yapen_id = models.CharField
    yapen_pass = models.CharField
    yogei_id = models.CharField
    yogei_pass = models.CharField
    naver_id = models.CharField
    naver_pass = models.CharField
    bnb_id = models.CharField
    bnb_pass = models.CharField

    class Meta:
        managed = False
        db_table = 'platforms_auth_info'

class StandardRoomsInfo(models.Model):
    appUser = models.ForeignKey(AppUser, on_delete=models.CASCADE)
    room_name = models.CharField
    display_order = models.IntegerField
    room_quantity = models.IntegerField

    class Meta:
        managed = False
        db_table = 'standard_rooms_info'

class PlatformsRoomsInfo(models.Model):
    appUser = models.ForeignKey(AppUser, on_delete=models.CASCADE)
    display_order = models.IntegerField
    standard_room_info = models.ForeignKey(StandardRoomsInfo, on_delete=models.CASCADE)
    yapen_room_name = models.CharField
    yogei_room_name = models.CharField
    naver_room_name = models.CharField
    bnb_room_name = models.CharField

    class Meta:
        managed = False
        db_table = 'platforms_rooms_info'
