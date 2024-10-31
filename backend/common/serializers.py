from rest_framework import serializers
from rest_framework.exceptions import ValidationError


class SetStandardRoomInfoSerializer(serializers.ModelSerializer):
    standard_room_name = serializers.CharField(source='standard_room_name', required=True)
    display_order = serializers.IntegerField(source='display_order', required=True)
    room_quantity = serializers.IntegerField(source='room_quantity', required=True)

    def validate(self, data):
        standard_room_name = data.get('standard_room_name')
        display_order = data.get('display_order')
        room_quantity = data.get('room_quantity')
        if display_order < 0:
            raise ValidationError({'display_order': display_order})
        if room_quantity < 0:
            raise ValidationError({'room_quantity': room_quantity})
        return data


    user = models.ForeignKey(User, on_delete=models.CASCADE)
    standard_room_info = models.ForeignKey(StandardRoomInfoModel, on_delete=models.CASCADE)
    yapen_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    yogei_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    naver_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)
    bnb_room_name = models.CharField(max_length=50, blank=True, null=True, unique=True)


class SetRoomInfoSerializer(serializers.ModelSerializer):
    standard_room_info_id = serializers.IntegerField(source='standard_room_info_id', required=True)
    yapen_room_name = serializers.CharField(source='yapen_room_name')
    yogei_room_name = serializers.CharField(source='yogei_room_name')
    naver_room_name = serializers.CharField(source='naver_room_name')
    bnb_room_name = serializers.CharField(source='bnb_room_name')

    def validate(self, data):
        standard_room_info_id = data.get('standard_room_info_id')
        yapen_room_name = data.get('yapen_room_name')
        yogei_room_name = data.get('yogei_room_name')
        naver_room_name = data.get('naver_room_name')
        bnb_room_name = data.get('bnb_room_name')
        if yapen_room_name is None and yogei_room_name is None and naver_room_name is None and bnb_room_name is None:
            raise ValidationError({'room_namse': 'ALL NONE'})
        return data
