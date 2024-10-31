from django.contrib.auth.models import User
from django.db import models

# Create your models here.
class PlatformAuthInfoModel(models.Model):
    # user = models.ForeignKey(User, on_delete=models.CASCADE, unique=True)
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    yapen_id = models.CharField(max_length=50, blank=True, null=True)
    yapen_pass = models.CharField(max_length=50, blank=True, null=True)
    yogei_id = models.CharField(max_length=50, blank=True, null=True)
    yogei_pass = models.CharField(max_length=50, blank=True, null=True)
    naver_id = models.CharField(max_length=50, blank=True, null=True)
    naver_pass = models.CharField(max_length=50, blank=True, null=True)
    bnb_id = models.CharField(max_length=50, blank=True, null=True)
    bnb_pass = models.CharField(max_length=50, blank=True, null=True)

class PlatformRoomInfoModel(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    display_order = models.IntegerField(blank=False, null=False)
    default_room_name = models.CharField(max_length=50, blank=False, null=False, unique=True)
    yapen_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    yogei_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    naver_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    bnb_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)

    class Meta:
        constraints = [
            models.UniqueConstraint(fields=['user', 'display_order'], name='unique_display_order'),
        ]


class StandardRoomInfoModel(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    standard_room_name = models.CharField(max_length=50, blank=False, null=False, unique=True)
    display_order = models.IntegerField(blank=False, null=False)
    room_quantity = models.IntegerField(blank=False, null=False)

    class Meta:
        constraints = [
            models.UniqueConstraint(fields=['user', 'display_order'], name='unq_display_order'),
        ]


class RoomInfoModel(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    standard_room_info = models.ForeignKey(StandardRoomInfoModel, on_delete=models.CASCADE)
    yapen_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    yogei_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    naver_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    bnb_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
