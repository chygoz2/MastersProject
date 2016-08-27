
default: claw3 diamond3 k43 simplicial3 triangle3 claw4 diamond4 k44 simplicial4 triangle4

instances.txt:  java Generator 1 1000 5 10 
		ls -1 generated_graphs > instances.txt

include claw.mk diamond.mk k4.mk simplicial.mk triangle.mk claw2.mk diamond2.mk k42.mk simplicial2.mk triangle2.mk claw3.mk diamond3.mk k43.mk simplicial3.mk triangle3.mk claw4.mk diamond4.mk k44.mk simplicial4.mk triangle4.mk


