import base64
import logging
from django.shortcuts import get_object_or_404, render
from django.contrib.auth.hashers import make_password
from django.contrib.auth import get_user_model

# Create your views here.
from django.shortcuts import render, redirect
from django.contrib.auth import login, authenticate, logout
from django.contrib import messages
from django.urls import reverse
from .forms import CustomAuthenticationForm, EditUserProfileForm, RegistrationForm, ScrollUploadForm
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import status
from .models import CustomUser 
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse
from django.contrib.auth.decorators import login_required
from django.contrib.auth import update_session_auth_hash
from django.contrib.auth.forms import PasswordChangeForm
import requests
from django.contrib.auth.decorators import user_passes_test

SPRING_BOOT_API_URL = 'http://springboot-service:8081/api/admin/users'
SPRING_BOOT_API_URL_S = 'http://springboot-service:8081/api/admin'
SPRING_BOOT_API_BAN_URL = 'http://springboot-service:8081/api/admin/ban'
SPRING_BOOT_API_UNBAN_URL = 'http://springboot-service:8081/api/admin/unban'

CustomUser = get_user_model()
logger = logging.getLogger(__name__)

def login_view(request):
    if request.method == 'POST':
        form = CustomAuthenticationForm(request, data=request.POST)
        if form.is_valid():
            username = form.cleaned_data.get('username')
            password = form.cleaned_data.get('password')
            user = authenticate(username=username, password=password)
            if user is not None:
                if user.banned:
                    messages.error(request, "Your account has been banned. Please contact support.")
                    return redirect('login')
                login(request, user)
                if user.role == 'admin':
                    return redirect('/admin-page/')
                else:
                    return redirect('/user-page/')
            else:
                messages.error(request, "Invalid username or password.")
        else:
            messages.error(request, "Invalid username or password.")
    else:
        form = CustomAuthenticationForm()
    return render(request, 'login.html', {'form': form})



def register(request):
    if request.method == 'POST':
        form = RegistrationForm(request.POST)
        if form.is_valid():
            user = form.save()  
            login(request, user)  
            messages.success(request, "Registration successful.")
            return redirect('user_page')  
        else:
            messages.error(request, "Unsuccessful registration. Invalid information.")
    else:
        form = RegistrationForm()

    return render(request, 'register.html', {'form': form})



def admin_page(request):
    try:
        response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/popular')
        if response.status_code == 200:
            popular_scrolls = response.json()
        else:
            popular_scrolls = []
    except requests.exceptions.RequestException as e:
        popular_scrolls = []
        print(f"Error fetching popular scrolls: {e}")

    return render(request, 'admin_page.html', {
        'user': request.user, 
        'popular_scrolls': popular_scrolls
    })
    

def user_page(request):
    try:
        response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/popular')
        if response.status_code == 200:
            popular_scrolls = response.json()
        else:
            popular_scrolls = []
    except requests.exceptions.RequestException as e:
        popular_scrolls = []
        print(f"Error fetching popular scrolls: {e}")

    return render(request, 'user_page.html', {
        'user': request.user, 
        'popular_scrolls': popular_scrolls
    })

def logout_view(request):
    logout(request)
    return redirect('/login/')
class LogoutAPIView(APIView):
    def post(self, request):
        logout(request)
        return Response({"message": "Logged out successfully"}, status=status.HTTP_200_OK)
    
class RegisterAPIView(APIView):
    def post(self, request):
        form = RegistrationForm(request.data)
        if form.is_valid():
            user = form.save()
            login(request, user)
            return Response({"message": "Registration successful"}, status=status.HTTP_201_CREATED)
        return Response(form.errors, status=status.HTTP_400_BAD_REQUEST)
    
class LoginAPIView(APIView):
    def post(self, request):
        form = CustomAuthenticationForm(request, data=request.data)
        if form.is_valid():
            user = authenticate(username=form.cleaned_data.get('username'), password=form.cleaned_data.get('password'))
            if user is not None:
                login(request, user)
                return Response({"message": "Login successful", "role": user.role}, status=status.HTTP_200_OK)
        return Response({"message": "Invalid credentials"}, status=status.HTTP_400_BAD_REQUEST)
    
def guest_login_view(request):
  
    guest_user = CustomUser.objects.filter(idKey='guest').first()
        

    if guest_user:
        login(request, guest_user)
        try:
            response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/popular')
            if response.status_code == 200:
                popular_scrolls = response.json()
            else:
                popular_scrolls = []
        except requests.exceptions.RequestException as e:
            popular_scrolls = []
            print(f"Error fetching popular scrolls: {e}")

        return render(request, 'guest_page.html', {
            'user': request.user, 
            'popular_scrolls': popular_scrolls
        })
    
    messages.error(request, "Guest login failed.")
    print("Error")
    return redirect('login')

def guest_page(request):
    scrolls = scrolls.objects.all()  
    return render(request, 'guest_page.html', {'scrolls': scrolls})

@login_required  
def get_admin_info(request):
    user = request.user 
    
    return JsonResponse({
        'username': user.username,
        'fullname': user.fullName, 
        'email': user.email, 
        'phone': user.phoneNumber,
        'IDkey': user.idKey,
        'role': user.role,  
        'profile_picture': user.profile_picture.url if user.profile_picture else None
    })

@login_required  
def user_info(request):
    user = request.user
    
    return render(request, "user_detail.html", {'admin': user})

@login_required
def user_list(request):
    try:
        
        response = requests.get(SPRING_BOOT_API_URL)

        
        if response.status_code == 200:
            users = response.json()
            user1 = CustomUser.objects.all()
            if not users:
                logger.warning("No users found in the response.")
        else:
            users = []  
            logger.error(f"Failed to fetch users. HTTP Status Code: {response.status_code}, Reason: {response.reason}")
            return JsonResponse({'error': f"Failed to fetch users. Status code: {response.status_code}"}, status=500)

    except requests.exceptions.RequestException as e:
        
        users = []
        logger.error(f"Error connecting to the Spring Boot API: {e}")
        return JsonResponse({'error': f"Error connecting to the Spring Boot API: {str(e)}"}, status=500)
    return render(request, 'user_list.html', {'users': user1})

def add_user(request):
    if request.method == 'POST':
        
        username = request.POST['username']
        email = request.POST['email']
        password = request.POST['password']  
        phoneNumber = request.POST['phoneNumber']  
        idKey = request.POST['idKey']  
        role = request.POST['role']
        fullName = request.POST['fullName']  

        user = CustomUser.objects.create_user(username=username, email=email, password=password)
        
        
        user.phoneNumber = phoneNumber
        user.idKey = idKey
        user.fullName = fullName
        user.role = role
        user.save()

        return redirect('user_list')

    return render(request, 'add_user.html')



def delete_user(request, username):
    if request.method == 'POST':
        delete_url = f"{SPRING_BOOT_API_URL}/{username}"
        response = requests.delete(delete_url)

        if response.status_code == 204:
            return redirect('user_list')
        else:
            print(f"Error deleting user: {response.status_code}")

    return redirect('user_list')


@login_required
def edit_profile(request):
    user = request.user
    if request.method == 'POST':
        print("FILES: ", request.FILES)
        form = EditUserProfileForm(request.POST, request.FILES, instance=user)
        if form.is_valid():
            
            form.save()
            print("Form is valid and saved")
            return redirect('user_detail')
    else:
        form = EditUserProfileForm(instance=user)
    return render(request, 'edit_profile.html', {'form': form})

@login_required
def change_password(request):
    if request.method == 'POST':
        form = PasswordChangeForm(user=request.user, data=request.POST)
        if form.is_valid():
            user = form.save()  
            update_session_auth_hash(request, user)  
            return redirect('user_detail') 
    else:
        form = PasswordChangeForm(user=request.user)

    return render(request, 'change_password.html', {'form': form})

def upload_scroll(request):
    if request.method == 'POST':
        form = ScrollUploadForm(request.POST, request.FILES)
        if form.is_valid():
            title = form.cleaned_data['title']
            file = request.FILES['file']

            files = {'file': file}
            data = {'title': title, 'owner': request.user.username}

            try:
                response = requests.post(f'{SPRING_BOOT_API_URL_S}/scrolls/upload', files=files, data=data)
                if response.status_code == 201:
                    return redirect('scroll_list')
                else:
                    print(f"Failed to upload scroll. Status code: {response.status_code}")
                    print(f"Response: {response.text}")
            except Exception as e:
                print(f"Error uploading file: {e}")
        else:
            print("Form is not valid.")
    else:
        form = ScrollUploadForm()

    return render(request, 'upload_scroll.html', {'form': form})



@login_required
def list_scrolls(request):
    user_id = request.user.id
    # Get search parameters from the request
    owner = request.GET.get('owner')
    scroll_id = request.GET.get('id')
    title = request.GET.get('title')
    uploaded_at = request.GET.get('uploadedAt')

    params = {
        'owner': owner,
        'id': scroll_id,
        'title': title,
        'uploadedAt': uploaded_at
    }

    try:
        response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/list', params=params)
        likes_response = requests.get(f"{SPRING_BOOT_API_URL_S}/scrolls/likes", params={'userId': user_id})

        if response.status_code == 200 and likes_response.status_code == 200:
            scrolls = response.json()
            liked_scrolls = [like['id'] for like in likes_response.json()]
        else:
            scrolls = []
            liked_scrolls = []
            print(f"Failed to fetch scrolls or likes. Status code: {response.status_code}")

    except requests.exceptions.RequestException as e:
        print(f"Error connecting to the Spring Boot API: {e}")
        scrolls = []
        liked_scrolls = []

    # Render the scroll_list.html template, passing the scrolls and liked_scrolls to the template
    return render(request, 'scroll_list.html', {'scrolls': scrolls, 'liked_scrolls': liked_scrolls})

def search_scrolls(request):
    # Get search parameters from the request
    owner = request.GET.get('owner')
    scroll_id = request.GET.get('id')
    title = request.GET.get('title')
    uploaded_at = request.GET.get('uploadedAt')

    
    params = {
        'owner': owner,
        'id': scroll_id,
        'title': title,
        'uploadedAt': uploaded_at
    }

    try:
        response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/list', params=params)
        if response.status_code == 200:
            scrolls = response.json()
        else:
            scrolls = []
            print(f"Failed to fetch scrolls. Status code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        scrolls = []
        print(f"Error connecting to the Spring Boot API: {e}")

    return render(request, 'scroll_search_results.html', {'scrolls': scrolls})


# def preview_scroll_page(request, id):
#     response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/preview/{id}')
#     if response.status_code == 200:
#         scroll_content = response.text
#         mime_type = response.headers.get('Content-Type', '')

#         # Fallback to manual detection if MIME type is empty
#         if not mime_type:
#             # Infer MIME type from file extension
#             file_extension = id.split('.')[-1]  
#             mime_type, _ = mimetypes.guess_type(f"file.{file_extension}")

#         # Ensure MIME type is checked properly
#         is_text = mime_type and mime_type.startswith("text")
#         is_image = mime_type and mime_type.startswith("image")

#         return render(request, 'preview_scroll.html', {
#             'scroll_content': scroll_content,
#             'mime_type': mime_type,
#             'is_text': is_text,
#             'is_image': is_image,
#         })
#     else:
#         return HttpResponse("Scroll preview not available", status=404)
def preview_scroll_page(request, id):
    response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/preview/{id}')
    
    if response.status_code == 200:
        data = response.json()
        file_type = data.get('fileType')
        file_content = data.get('file')
        
        if not file_content:
            return HttpResponse("File content is empty.", status=400)

        # Handle the content based on the file type
        if file_type in ["jpg", "png", "jpeg"]:
            # Pass Base64 data directly for images
            return render(request, 'preview_scroll.html', {
                'image_data': file_content, 
                'file_type': file_type
            })
        elif file_type == "pdf" or file_type == "docx":
            # Pass Base64 data directly for PDFs
            return render(request, 'preview_scroll.html', {
                'pdf_data': file_content, 
                'file_type': file_type
            })
        elif file_type == "txt":
            # Decode Base64 string to plain text
            text_content = base64.b64decode(file_content).decode('utf-8')
            return render(request, 'preview_scroll.html', {
                'text_data': text_content, 
                'file_type': file_type
            })
        else:
            return HttpResponse("Unsupported file type.", status=415)

    return HttpResponse("Scroll not found.", status=404)


@login_required
def delete_scroll(request, scroll_id):
    if request.method == 'POST':
        delete_url = f'{SPRING_BOOT_API_URL_S}/scrolls/delete/{scroll_id}'
        response = requests.delete(delete_url)

        if response.status_code == 204:
            return redirect('your_scrolls')
        else:
            return JsonResponse({'message': f'Error deleting scroll: {response.status_code}'}, status=500)

    return redirect('your_scrolls')


@login_required
def like_scroll(request, scroll_id, user_id):
    next_page = request.GET.get('next', 'your_scrolls')
    if request.method == 'POST':
        try:
            response = requests.post(f"{SPRING_BOOT_API_URL_S}/scrolls/like", data={'userId': user_id, 'scrollId': scroll_id})
            if response.status_code == 200:
                return redirect(next_page)
            else:
                return JsonResponse({'message': f'Error liking scroll: {response.status_code}'}, status=response.status_code)
        except requests.exceptions.RequestException as e:
            return JsonResponse({'message': f'Error liking scroll: {str(e)}'}, status=500)
    return JsonResponse({'message': 'Invalid request method.'}, status=400)

@login_required
def unlike_scroll(request, scroll_id, user_id):
    next_page = request.GET.get('next', 'your_scrolls')
    if request.method == 'POST':
        try:
            response = requests.post(f"{SPRING_BOOT_API_URL_S}/scrolls/unlike", data={'userId': user_id, 'scrollId': scroll_id})
            if response.status_code == 200:
                return redirect(next_page)
            else:
                return JsonResponse({'message': f'Error unliking scroll: {response.status_code}'}, status=response.status_code)
        except requests.exceptions.RequestException as e:
            return JsonResponse({'message': f'Error unliking scroll: {str(e)}'}, status=500)
    return JsonResponse({'message': 'Invalid request method.'}, status=400)

@login_required
def get_likes_by_user(request, user_id):
    try:
        response = requests.get(f"{SPRING_BOOT_API_URL_S}/scrolls/likes", params={'userId': user_id})
        if response.status_code == 200:
            liked_scrolls = response.json()
            return render(request, 'liked_scrolls.html', {'liked_scrolls': liked_scrolls})
        else:
            return JsonResponse({'message': f'Error fetching likes: {response.status_code}'}, status=response.status_code)
    except requests.exceptions.RequestException as e:
        return JsonResponse({'message': f'Error fetching likes: {str(e)}'}, status=500)
@login_required
def liked_scrolls(request, user_id):
    try:
        response = requests.get(f"{SPRING_BOOT_API_URL_S}/scrolls/likes", params={'userId': user_id})
        if response.status_code == 200:
            liked_scrolls = response.json()
            return render(request, 'liked_scrolls.html', {'liked_scrolls': liked_scrolls})
        else:
            return JsonResponse({'message': f'Error fetching likes: {response.status_code}'}, status=response.status_code)
    except requests.exceptions.RequestException as e:
        return JsonResponse({'message': f'Error fetching likes: {str(e)}'}, status=500)    
def your_scrolls(request):
    try:
        username = request.user.username
        response = requests.get(f"{SPRING_BOOT_API_URL_S}/scrolls/list/{username}")
        
        if response.status_code == 200:
            scrolls = response.json()  
        elif response.status_code == 204:
            scrolls = []  
        else:
            scrolls = []
            print(f"Failed to fetch scrolls for {username}. Status code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Error fetching scrolls: {e}")
        scrolls = []

    return render(request, 'your_scroll.html', {'scrolls': scrolls})





def download_scroll(request, scroll_id):
    download_url = f'{SPRING_BOOT_API_URL_S}/scrolls/download/{scroll_id}'
    
    try:
        # Fetch the file from Spring Boot API
        response = requests.get(download_url, stream=True)
        if response.status_code == 200:
            content_type = response.headers.get('Content-Type', 'application/octet-stream')
            content_disposition = response.headers.get('Content-Disposition', f'attachment; filename="scroll_{scroll_id}"')
            
            # Create Django HttpResponse with file content
            django_response = HttpResponse(response.content, content_type=content_type)
            django_response['Content-Disposition'] = content_disposition
            return django_response
        
        return HttpResponse("Failed to download file.", status=response.status_code)
    
    except requests.exceptions.RequestException as e:
        return HttpResponse(f"An error occurred: {str(e)}", status=500)


@login_required
def edit_scroll_file(request, id):
    if request.method == 'POST':
        file_content = request.POST.get('file_content')  
        edit_url = f'{SPRING_BOOT_API_URL_S}/scrolls/edit/{id}'
        
        try:
            # Ensure file_content is being sent correctly
            response = requests.post(edit_url, data={'file_content': file_content})
            
            if response.status_code == 200:
                return redirect('your_scrolls')
            else:
                return HttpResponse(f"Failed to edit scroll. Status code: {response.status_code}", status=response.status_code)
        
        except Exception as e:
            return HttpResponse(f"An error occurred: {str(e)}", status=500)
    
    # Fetch the existing scroll content to display in the form
    response = requests.get(f'{SPRING_BOOT_API_URL_S}/scrolls/preview/{id}')
    
    if response.status_code == 200:
        scroll_content = response.text
        return render(request, 'edit_scroll.html', {'scroll_content': scroll_content, 'scroll_id': id})
    else:
        return HttpResponse("Scroll preview not available", status=404)
    
# Ban users from app
def is_admin(user):
    return user.is_authenticated and user.role == 'admin'
# Only allow admins to access this view
@login_required
def ban_user(request, username):
    try:
        response = requests.post(f"{SPRING_BOOT_API_BAN_URL}/{username}")

        if response.status_code == 200:
            user = CustomUser.objects.get(username=username)
            user.banned = True
            user.save()
            messages.success(request, f"User {username} banned successfully.")
        else:
            messages.error(request, f"Failed to ban user {username}. Status code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        logger.error(f"Error banning user {username}: {e}")
        messages.error(request, f"Error banning user {username}. Exception: {e}")

    return redirect('user_list')


@login_required
def unban_user(request, username):
    try:
        response = requests.post(f"{SPRING_BOOT_API_UNBAN_URL}/{username}")

        if response.status_code == 200:
            user = CustomUser.objects.get(username=username)
            user.banned = False
            user.save()
            messages.success(request, f"User {username} unbanned successfully.")
        else:
            messages.error(request, f"Failed to unban user {username}. Status code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        logger.error(f"Error unbanning user {username}: {e}")
        messages.error(request, f"Error unbanning user {username}. Exception: {e}")

    return redirect('user_list')

    

