from django.db import models

class AppUser(models.Model):
    username = models.CharField(max_length=50)
    password = models.CharField(max_length=50)
    email = models.CharField(max_length=50)

    class Meta:
        managed = False  # Django가 이 테이블을 관리하지 않도록 설정
        db_table = 'app_user'  # 기존 데이터베이스 테이블 이름

class PlatformsAuthInfo(models.Model):
    appUser = models.OneToOneField(AppUser, on_delete=models.CASCADE, db_column='app_user_id')
    yapen_id = models.CharField(max_length=50)
    yapen_pass = models.CharField(max_length=50)
    yogei_id = models.CharField(max_length=50)
    yogei_pass = models.CharField(max_length=50)
    naver_id = models.CharField(max_length=50)
    naver_pass = models.CharField(max_length=50)
    bnb_id = models.CharField(max_length=50)
    bnb_pass = models.CharField(max_length=50)

    class Meta:
        managed = False
        db_table = 'platforms_auth_info'

class StandardRoomsInfo(models.Model):
    appUser = models.ForeignKey(AppUser, on_delete=models.CASCADE, db_column='app_user_id')
    room_name = models.CharField(max_length=50)
    display_order = models.IntegerField()
    room_quantity = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'standard_rooms_info'

class PlatformsRoomsInfo(models.Model):
    appUser = models.ForeignKey(AppUser, on_delete=models.CASCADE, db_column='app_user_id')
    standard_room_info = models.ForeignKey(StandardRoomsInfo, on_delete=models.CASCADE, db_column='standard_room_info_id')
    display_order = models.IntegerField()
    yapen_room_name = models.CharField(max_length=50)
    yogei_room_name = models.CharField(max_length=50)
    naver_room_name = models.CharField(max_length=50)
    bnb_room_name = models.CharField(max_length=50)

    class Meta:
        managed = False
        db_table = 'platforms_rooms_info'

class PreviousInfo(models.Model):
    appUser = models.ForeignKey(AppUser, on_delete=models.CASCADE, db_column='app_user_id')
    standard_room_info = models.ForeignKey(StandardRoomsInfo, on_delete=models.CASCADE, db_column='standard_room_info_id')
    yapen_booked = models.IntegerField()
    yogei_booked = models.IntegerField()
    naver_booked = models.IntegerField()
    bnb_booked = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'previous_info'
