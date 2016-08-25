JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances2.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

k4: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_k4_detection_result/$(i))

$(RESULTSDIR)/bruteforce_k4_detection_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_k4_detection_result
	$(JVM) bruteforce.detection.DetectK4 $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_k4_detection_result/$*
