from django.http import JsonResponse
from rest_framework.authentication import TokenAuthentication
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.status import HTTP_200_OK, HTTP_400_BAD_REQUEST

from api.bot import bot_integrated
from api.models import PlatformInfoModel


# Create your views here.

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def set_yapen(request):
    yapen_id = request.data['yapen_id']
    yapen_pass = request.data['yapen_pass']
    user = request.user

    info = PlatformInfoModel.objects.filter(user=user)
    if not info.exists():
        PlatformInfoModel.objects.create(user=user, yapen_id=yapen_id, yapen_pass=yapen_pass)
    else:
        info.update(yapen_id=yapen_id, yapen_pass=yapen_pass)

    return JsonResponse({'message': 'yapen setting success'}, status=HTTP_200_OK)

@api_view(['POST'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def set_yogei(request):
    yogei_id = request.data['yogei_id']
    yogei_pass = request.data['yogei_pass']
    user = request.user

    info = PlatformInfoModel.objects.filter(user=user)
    if not info.exists():
        PlatformInfoModel.objects.create(user=user, yogei_id=yogei_id, yogei_pass=yogei_pass)
    else:
        info.update(yogei_id=yogei_id, yogei_pass=yogei_pass)

    return JsonResponse({'message': 'yogei setting success'}, status=HTTP_200_OK)

@api_view(['GET'])
@authentication_classes([TokenAuthentication])
@permission_classes([IsAuthenticated])
def retrieve_info(request):
    months = request.data['months']
    user = request.user
    bot_integrated(months=months, user=user)

    return JsonResponse({'message': 'good'}, status=HTTP_200_OK)
