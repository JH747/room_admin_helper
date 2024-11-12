from rest_framework import serializers
from rest_framework.exceptions import ValidationError

from api.models import StandardRoomInfoModel


# class SetStandardRoomInfoSerializer(serializers.Serializer):
#     id = serializers.IntegerField(read_only=True)
#     standard_room_name = serializers.CharField(required=True)
#     display_order = serializers.IntegerField(required=True)
#     room_quantity = serializers.IntegerField(required=True)
#
#     def validate(self, data):
#         standard_room_name = data.get('standard_room_name')
#         display_order = data.get('display_order')
#         room_quantity = data.get('room_quantity')
#         if display_order < 0:
#             raise ValidationError({'display_order': display_order})
#         if room_quantity < 0:
#             raise ValidationError({'room_quantity': room_quantity})
#         return data
#
#
#
# class SetRoomInfoSerializer(serializers.Serializer):
#     id = serializers.IntegerField(read_only=True)
#     standard_room_info_id = serializers.IntegerField(required=True)
#     standard_room_name = serializers.SerializerMethodField()  # 추가된 부분
#     yapen_room_name = serializers.CharField(required=False)
#     yogei_room_name = serializers.CharField(required=False)
#     naver_room_name = serializers.CharField(required=False)
#     bnb_room_name = serializers.CharField(required=False)
#
#     def get_standard_room_name(self, obj):
#         # standard_room_info_id에 해당하는 StandardRoomInfoModel의 이름 반환
#         return obj.standard_room_info.standard_room_name if obj.standard_room_info else None
#
#     def validate(self, data):
#         standard_room_info_id = data.get('standard_room_info_id')
#         yapen_room_name = data.get('yapen_room_name')
#         yogei_room_name = data.get('yogei_room_name')
#         naver_room_name = data.get('naver_room_name')
#         bnb_room_name = data.get('bnb_room_name')
#         if yapen_room_name is None and yogei_room_name is None and naver_room_name is None and bnb_room_name is None:
#             raise ValidationError({'room_names': 'ALL NONE'})
#         return data
