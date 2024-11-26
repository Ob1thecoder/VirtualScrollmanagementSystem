"""
URL configuration for auth project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.0/topics/http/urls/
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
from django.contrib import admin
from django.urls import path
from django.conf import settings
from django.conf.urls.static import static
from authapp.views import LoginAPIView, RegisterAPIView, add_user, ban_user, change_password, \
    delete_scroll, delete_user, download_scroll, edit_profile, edit_scroll_file, \
        list_scrolls, preview_scroll_page, search_scrolls, upload_scroll,user_info, get_admin_info, \
            guest_login_view, guest_page, login_view, admin_page, user_list, user_page, logout_view,unban_user, LogoutAPIView,register, your_scrolls
from django.views.generic import RedirectView

urlpatterns = [
    path('admin/', admin.site.urls),
    path('login/', login_view, name='login'),
    path('users/ban/<str:username>/', ban_user, name='ban_user'),
    path('users/unban/<str:username>/', unban_user, name='unban_user'),
    path('register/', register, name='register'),
    path('logout/', logout_view, name='logout'),
    path('', RedirectView.as_view(url='/login/', permanent=False)),
    path('logout-api/', LogoutAPIView.as_view(), name='logout_api'),
    path('login-api/', LoginAPIView.as_view(), name='login_api'),
    path('register-api/', RegisterAPIView.as_view(), name='register_api'),
    path('admin-page/', admin_page, name='admin_page'),
    path('user-page/', user_page, name='user_page'),
    path('guest-login/', guest_login_view, name='guest_login'), 
    path('admin-info/', get_admin_info, name='admin_info'),
    path('user_detail', user_info, name="user_detail"),
    path('users/', user_list, name='user_list'),
    path('users/add/', add_user, name='add_user'),
    path('users/delete/<str:username>/', delete_user, name='delete_user'),
    path('edit-profile/', edit_profile, name='edit_profile'),
    path('change-password/', change_password, name='change_password'), 
    path('scrolls/', list_scrolls, name='scroll_list'),
    path('scrolls/upload/', upload_scroll, name='upload_scroll'),
    path('scrolls/search/', search_scrolls, name='search_scrolls'),
    path('scrolls/preview/<int:id>/', preview_scroll_page, name='preview_scroll_page'),
    path('scrolls/delete/<int:scroll_id>/', delete_scroll, name='delete_scroll'),
    path('your-scrolls/', your_scrolls, name='your_scrolls'),
    path('scrolls/download/<int:scroll_id>/', download_scroll, name='download_scroll'),
    path('scrolls/edit/<int:id>/', edit_scroll_file, name='edit_scroll'),

    path('guest-page/', guest_page, name='guest_page')
    
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT) + static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
