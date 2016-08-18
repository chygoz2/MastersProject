JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

triangle: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_triangle_detection_result/$(i))

$(RESULTSDIR)/efficient_triangle_detection_result/%:
	mkdir -p $(RESULTSDIR)/efficient_triangle_detection_result
	$(JVM) efficientdetection/DetectTriangle $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_triangle_detection_result/$*

