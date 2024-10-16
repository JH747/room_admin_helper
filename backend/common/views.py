from django.contrib.auth.models import User
from django.http import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.authtoken.admin import Token
from rest_framework.status import HTTP_400_BAD_REQUEST, HTTP_200_OK, HTTP_401_UNAUTHORIZED
from common.models import EmailVerifyCodeModel
from common.utils import send_email

# Create your views here.

@api_view(['POST'])
def verify_email(request):
    try:
        send_email(request.data['email'])
        return JsonResponse({'message': 'mail successfully sent to given address'}, status = HTTP_200_OK)

    except Exception as e:
        return JsonResponse({'message': str(e)}, status = HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def signup(request):
    try:
        username = request.data['id']
        password = request.data['pass']
        email = request.data['email']
        given_code = request.data['code']
        right_code_ = EmailVerifyCodeModel.objects.filter(email=email)

        if not right_code_.exists():
            return JsonResponse({'message': 'code expired or not sent'}, status=HTTP_400_BAD_REQUEST)
        right_code = right_code_.first().code
        if given_code != right_code:
            return JsonResponse({'message': 'wrong code'}, status=HTTP_400_BAD_REQUEST)

        user = User.objects.create_user(username=username, password=password, email=email)
        user.save()
        Token.objects.create(user)
        return JsonResponse({'message': 'signup success'}, status=200)

    except Exception as e:
        return JsonResponse({'message': str(e)}, status=HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def login(request):
    try:
        username = request.data['id']
        password = request.data['pass']
        user = User.objects.get(username=username)

        if user.check_password(password):
            token = Token.objects.get(user=user)
            return JsonResponse({'token': token.key}, status=HTTP_200_OK)
        else:
            return JsonResponse({'message': 'wrong password'}, status=HTTP_401_UNAUTHORIZED)

    except Exception as e:
        return JsonResponse({'message': str(e)}, status=HTTP_400_BAD_REQUEST)

