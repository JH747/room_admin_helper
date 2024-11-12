import string
import random

from django.utils import timezone
from datetime import timedelta

from config import settings
import smtplib
from email.mime.text import MIMEText

# def expire_outdated_codes():
#     five_minutes_ago = timezone.now() - timedelta(minutes=5)
#     old_codes = EmailVerifyCodeModel.objects.filter(created_at__lt=five_minutes_ago)
#     old_codes.delete()
#     return
#
# def code_generator():
#     characters = string.ascii_letters + string.digits
#     code = ''
#     for i in range(6):
#         code += random.choice(characters)
#     return code
#
# def save_code(email, code):
#     EmailVerifyCodeModel.objects.filter(email=email).delete() # deleted existing code if duplicates
#     young_code = EmailVerifyCodeModel(email=email, code=code)
#     young_code.save()
#     return
#
# def send_email(addr):
#     smtp_host = settings.SMTP_HOST
#     smtp_port = settings.SMTP_PORT
#     smtp_user = settings.SMTP_USER
#     smtp_password = settings.SMTP_PASSWORD
#     receiver_email = addr
#     genned_code = code_generator()
#     save_code(receiver_email, genned_code)
#
#     smtp = smtplib.SMTP(smtp_host, smtp_port)
#     smtp.starttls()
#     smtp.login(smtp_user, smtp_password)
#
#     msg = MIMEText(genned_code)
#     msg['From'] = smtp_user
#     msg['To'] = receiver_email
#     msg['Subject'] = '[room-admin-helper] Email Verification Code'
#     smtp.sendmail(smtp_user, receiver_email, msg.as_string())
#     smtp.quit()
#     return

