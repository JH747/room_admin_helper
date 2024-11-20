"""
URL configuration for config project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.urls import path
from . import views

app_name = 'api'
urlpatterns = [
    path('tester/', views.test_view, name='test_view'),
    # path('setplatformauth/', views.set_platform_auth, name='set_platform_auth'),
    # path('setstandardroominfo/', views.SetStandardRoomInfoView.as_view(), name='set_standard_room_info'),
    # path('setroominfo/', views.SetRoomInfoView.as_view(), name='set_room_info'),
    path('detect/', views.detect, name='detect'),
    path('retrieve/', views.retrieve, name='retrieve'),
    path('supply_warn/', views.supply_warn, name='supply_warn'),
]
