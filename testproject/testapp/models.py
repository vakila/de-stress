from django.db import models

# Create your models here.
class Exercise(models.Model):
    name = models.CharField(max_length=20)
    text = models.CharField(max_length=200)
    date_created = models.DateTimeField()
    def __unicode__(self):
        return self.name



class Lesson(models.Model):
    name = models.CharField(max_length=20)
    description = models.TextField()
    date_created = models.DateTimeField()
    #exercises = models.ManyToManyField(Exercise)
    def __unicode__(self):
        return self.name
