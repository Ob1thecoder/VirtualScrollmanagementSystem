from django import forms
from django.contrib.auth.forms import AuthenticationForm, UserCreationForm, PasswordChangeForm
from django.contrib.auth import authenticate
from .models import CustomUser
from PIL import Image
from io import BytesIO
from django.core.files.base import ContentFile
import os

class CustomAuthenticationForm(AuthenticationForm):
    username = forms.CharField(max_length=254, widget=forms.TextInput(attrs={'class': 'form-control', 'placeholder': 'Username'}))
    password = forms.CharField(widget=forms.PasswordInput(attrs={'class': 'form-control', 'placeholder': 'Password'}))

class RegistrationForm(UserCreationForm):
    class Meta:
        model = CustomUser
        fields = ['username', 'idKey', 'fullName', 'email', 'phoneNumber', 'password1', 'password2']
    
    def clean(self):
        cleaned_data = super().clean()
        password1 = cleaned_data.get("password1")
        password2 = cleaned_data.get("password2")
        
        if password1 and password2 and password1 != password2:
            raise forms.ValidationError("Passwords do not match")
class EditUserProfileForm(forms.ModelForm):
    # password = forms.CharField(widget=forms.PasswordInput(), required=False)

    class Meta:
        model = CustomUser
        fields = ['idKey', 'phoneNumber', 'fullName', 'profile_picture']
        widgets = {
            'profile_picture': forms.FileInput(),
        }

    def clean_idKey(self):
        idKey = self.cleaned_data['idKey']
        if CustomUser.objects.filter(idKey=idKey).exists() and self.instance.idKey != idKey:
            raise forms.ValidationError('This ID key is already taken.')
        return idKey

    def save(self, commit=True):
        user = super().save(commit=False)
        old_profile_picture = None
        try:
            old_prof = CustomUser.objects.get(idKey=user.idKey)
            if old_prof.profile_picture:
                old_profile_picture = old_prof.profile_picture.path
        except CustomUser.DoesNotExist:
            pass
        if 'profile_picture' in self.cleaned_data and self.cleaned_data['profile_picture']:
            # Process the new profile picture
            profile_picture = self.cleaned_data['profile_picture']
            image = Image.open(profile_picture)
            image.thumbnail((200, 200), Image.LANCZOS)
            # Modify the file name to avoid overly long paths
            filename = os.path.basename(profile_picture.name)
            shortened_name = f"short_{filename}"
            img_io = BytesIO()
            image.save(img_io, format=image.format)
            image_content = ContentFile(img_io.getvalue(), name=shortened_name)
            user.profile_picture = image_content

        # Save the new user instance
        if commit:
            user.save()
            # Delete the old profile picture if a new one has been uploaded
            if old_profile_picture and old_profile_picture != user.profile_picture.path:
                if os.path.exists(old_profile_picture):
                    os.remove(old_profile_picture)

        return user
        
    
class ScrollUploadForm(forms.Form):
    title = forms.CharField(max_length=255)
    file = forms.FileField()




