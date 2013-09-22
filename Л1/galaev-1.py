# -*- coding: utf-8 -*-

import os

class Point:
    x, y = None, None
    def __init__(self, x, y):
        self.x = x
        self.y = y

def direction(pi, pj, pk):
    return (pj.x - pi.x)*(pk.y - pi.y) - (pk.x - pi.x)*(pj.y - pi.y)

def onSegment(pi, pj, pk):
    if min(pi.x, pj.x) <= pk.x <= max(pi.x, pj.x) and min(pi.y, pj.y) <= pk.y <= max(pi.y, pj.y):
        return True
    else:
        return False

def segmentsIntersect(p1, p2, p3, p4):
    d1 = direction(p3, p4, p1)
    d2 = direction(p3, p4, p2)
    d3 = direction(p1, p2, p3)
    d4 = direction(p1, p2, p4)
    if ((d1 > 0 and d2 < 0) or (d1 < 0 and d2 > 0)) and ((d3 > 0 and d4 < 0) or (d3 < 0 and d4 > 0)):
        return True
    elif d1 == 0 and onSegment(p3, p4, p1):
        return True
    elif d2 == 0 and onSegment(p3, p4, p2):
        return True
    elif d3 == 0 and onSegment(p1, p2, p3):
        return True
    elif d4 == 0 and onSegment(p1, p2, p4):
        return True
    else:
        return False

print u'Введите координаты первого отрезка:'
x1 = int(raw_input("x1 ="))
y1 = int(raw_input("y1 ="))
x2 = int(raw_input("x2 ="))
y2 = int(raw_input("y2 ="))
print u'Введите координаты второго отрезка:'
x3 = int(raw_input("x3 ="))
y3 = int(raw_input("y3 ="))
x4 = int(raw_input("x4 ="))
y4 = int(raw_input("y4 ="))
p1 = Point(x1, y1)
p2 = Point(x2, y2)
p3 = Point(x3, y3)
p4 = Point(x4, y4)

answer = segmentsIntersect(p1,p2,p3,p4)

if answer:
    print u"Отрезки пересекаются"
else:
    print u"Отрезки не пересекаются"

os.system("pause")