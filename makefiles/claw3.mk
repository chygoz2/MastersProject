JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 trimmedListInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

claw3: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_claw_listing_result/$(i))

$(RESULTSDIR)/efficient_claw_listing_result/%:
	mkdir -p $(RESULTSDIR)/efficient_claw_listing_result
	$(JVM) efficient.listing.ListClaws $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_claw_listing_result/$*
