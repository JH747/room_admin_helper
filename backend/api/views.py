from django.http import JsonResponse
from django.http import StreamingHttpResponse

from rest_framework.authentication import TokenAuthentication
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.status import HTTP_200_OK, HTTP_400_BAD_REQUEST, HTTP_500_INTERNAL_SERVER_ERROR, HTTP_201_CREATED
from rest_framework.views import APIView

from api.models import AppUser
from api.bot import bot_integrated

from datetime import datetime
import threading
import time
import json

# Create your views here.

@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def test_view(request):

    return JsonResponse({'message': 'good'})

# @api_view(['PUT'])
# @authentication_classes([TokenAuthentication])
# @permission_classes([IsAuthenticated])
# def set_platform_auth(request):
#     user = request.user
#     yapen_id = request.data.get('yapen_id')
#     yapen_pass = request.data.get('yapen_pass')
#     yogei_id = request.data.get('yogei_id')
#     yogei_pass = request.data.get('yogei_pass')
#     naver_id = request.data.get('naver_id')
#     naver_pass = request.data.get('naver_pass')
#     bnb_id = request.data.get('bnb_id')
#     bnb_pass = request.data.get('bnb_pass')
#
#     auth_info, created = PlatformAuthInfoModel.objects.update_or_create(
#         user=user,
#         defaults={
#             'yapen_id': yapen_id,
#             'yapen_pass': yapen_pass,
#             'yogei_id': yogei_id,
#             'yogei_pass': yogei_pass,
#             'naver_id': naver_id,
#             'naver_pass': naver_pass,
#             'bnb_id': bnb_id,
#             'bnb_pass': bnb_pass,
#         }
#     )
#     response_str = 'created' if created else 'updated'
#     return JsonResponse({'message': f'platform auth setting {response_str}'},
#                         status=HTTP_201_CREATED if created else HTTP_200_OK)

# @authentication_classes([TokenAuthentication])
# @permission_classes([IsAuthenticated])
# class SetStandardRoomInfoView(APIView):
#     def get(self, request):
#         user = request.user
#         target_standard_room_infos = StandardRoomInfoModel.objects.filter(user=user)
#         response_data = SetStandardRoomInfoSerializer(target_standard_room_infos, many=True).data
#         return JsonResponse(response_data, status=HTTP_200_OK, safe=False)
#
#     def post(self, request):
#         user = request.user
#         serializer = SetStandardRoomInfoSerializer(data=request.data)
#         serializer.is_valid(raise_exception=True)
#         standard_room_name = serializer.validated_data['standard_room_name']
#         display_order = serializer.validated_data['display_order']
#         room_quantity = serializer.validated_data['room_quantity']
#         StandardRoomInfoModel.objects.create(user=user, standard_room_name=standard_room_name, display_order=display_order, room_quantity=room_quantity)
#         return JsonResponse({'message': 'successfully created'}, status=HTTP_201_CREATED)
#
#     def delete(self, request):
#         user = request.user
#         standard_room_info_id = request.data.get('standard_room_info_id')
#         StandardRoomInfoModel.objects.get(id=standard_room_info_id).delete()
#         return JsonResponse({'message': 'successfully deleted'}, status=HTTP_200_OK)

# @authentication_classes([TokenAuthentication])
# @permission_classes([IsAuthenticated])
# class SetRoomInfoView(APIView):
#     def get(self, request):
#         user = request.user
#         target_room_infos = RoomInfoModel.objects.filter(user=user)
#         response_data = SetRoomInfoSerializer(target_room_infos, many=True).data
#         return JsonResponse(response_data, status=HTTP_200_OK, safe=False)
#
#     def post(self, request):
#         user = request.user
#         serializer = SetRoomInfoSerializer(data=request.data)
#         serializer.is_valid(raise_exception=True)
#         validated_data = serializer.validated_data
#         effective_data = {key: value for key, value in validated_data.items() if value is not None}
#         RoomInfoModel.objects.create(user=user, **effective_data)
#         return JsonResponse({'message': 'successfully created'}, status=HTTP_201_CREATED)
#
#     def delete(self, request):
#         user = request.user
#         room_info_id = request.data.get('room_info_id')
#         RoomInfoModel.objects.get(id=room_info_id).delete()
#         return JsonResponse({'message': 'successfully deleted'}, status=HTTP_200_OK)

@api_view(['GET'])
def retrieve_info(request):
    username = request.query_params.get('username')
    start_date = datetime.strptime(request.query_params.get('start_date'), '%Y-%m-%d')
    end_date = datetime.strptime(request.query_params.get('end_date'), '%Y-%m-%d')
    detector_mode = True if request.query_params.get('detector_mode') == 'yes' else False

    app_user = AppUser.objects.get(username=username)

    # result = None
    # err = None
    # completed = threading.Event()
    #
    # def run_bot():
    #     nonlocal result, err, completed, start_date, end_date, detector_mode, app_user
    #     try:
    #         # bot_integrated 실행 및 결과 저장
    #         result = bot_integrated(app_user=app_user, start_date=start_date, end_date=end_date, detector_mode=detector_mode)
    #     except Exception as e:
    #         # 에러가 발생하면 에러 메시지 저장
    #         err = str(e)
    #     finally:
    #         # 작업이 완료되었음을 알림
    #         completed.set()
    #
    # def event_stream():
    #     nonlocal result, err, completed
    #     yield f'data: {json.dumps({"status": "processing", "message": "Processing started"})}\n\n'
    #     time.sleep(1)
    #
    #     # 별도의 스레드에서 bot_integrated 실행
    #     bot_thread = threading.Thread(target=run_bot)
    #     bot_thread.start()
    #
    #     # bot_integrated가 실행되는 동안 heartbeat 전송
    #     while not completed.is_set():
    #         yield f'data: {json.dumps({"status": "heartbeat", "message": "Processing"})}\n\n'
    #         time.sleep(5)  # 5초마다 heartbeat 전송
    #
    #     # 작업 완료 후 결과 전송
    #     if err:
    #         yield f'data: {json.dumps({"status": "error", "message": err})}\n\n'
    #     else:
    #         yield f'data: {json.dumps({"status": "success", "message": "Process completed successfully", "result": result}, ensure_ascii=False)}\n\n'
    #     # 최종적으로 연결을 종료 신호 전송
    #     yield f'data: {json.dumps({"status": "closed", "message": "Connection closed"})}\n\n'
    #
    # # StreamingHttpResponse로 클라이언트와의 연결을 유지
    # response = StreamingHttpResponse(event_stream(), content_type='text/event-stream')
    # response['Cache-Control'] = 'no-cache'
    # response.status_code = HTTP_200_OK if not err else HTTP_500_INTERNAL_SERVER_ERROR

    result = bot_integrated(app_user=app_user, start_date=start_date, end_date=end_date, detector_mode=detector_mode)

    return JsonResponse(result, safe=False, status=HTTP_200_OK)
