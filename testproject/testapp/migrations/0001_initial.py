# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Exercise',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, help_text='', verbose_name='ID', primary_key=True)),
                ('name', models.CharField(max_length=20, help_text='')),
                ('text', models.CharField(max_length=200, help_text='')),
                ('date_created', models.DateTimeField(help_text='')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Lesson',
            fields=[
                ('id', models.AutoField(serialize=False, auto_created=True, help_text='', verbose_name='ID', primary_key=True)),
                ('name', models.CharField(max_length=20, help_text='')),
                ('description', models.TextField(help_text='')),
                ('date_created', models.DateTimeField(help_text='')),
                ('exercises', models.ManyToManyField(help_text='', to='testapp.Exercise')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
