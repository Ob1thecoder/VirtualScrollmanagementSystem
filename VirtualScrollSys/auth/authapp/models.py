from django.db import models
from django.contrib.auth.models import AbstractUser

# Create your models here.
class CustomUser(AbstractUser):
    ADMIN = 'admin'
    USER = 'user'
    
    ROLE_CHOICES = [
        (ADMIN, 'Admin'),
        (USER, 'User'),
    ]
    idKey = models.CharField(max_length=100, unique=True)
    role = models.CharField(max_length=5, choices=ROLE_CHOICES, default=USER)
    fullName = models.CharField(max_length=255)
    phoneNumber = models.CharField(max_length=20)
    email = models.EmailField(unique=True)
    profile_picture = models.ImageField(upload_to='profile_pictures/', null=True, blank=True)
    banned = models.BooleanField(default=False)
    

    def __str__(self):
        return self.fullName