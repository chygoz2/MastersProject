JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 trimmedListInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

claw4: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_claw_listing_result/$(i))

$(RESULTSDIR)/bruteforce_claw_listing_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_claw_listing_result
	$(JVM) bruteforce.listing.ListClaws $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_claw_listing_result/$*
