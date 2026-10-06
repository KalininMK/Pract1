JAVAC ?= javac
SRC := $(shell find src -name '*.java')
TESTS := $(shell find tests -name '*.java')
MAIN_DIR := build/main
TEST_DIR := build/test

.PHONY: all build test run clean

all: build

build:
	mkdir -p $(MAIN_DIR)
	$(JAVAC) -d $(MAIN_DIR) $(SRC)

test: build
	mkdir -p $(TEST_DIR)
	$(JAVAC) -cp $(MAIN_DIR) -d $(TEST_DIR) $(TESTS)
	java -cp $(MAIN_DIR):$(TEST_DIR) emulator.TestMain

run: build
	java -cp $(MAIN_DIR) emulator.Main $(ARGS)

clean:
	rm -rf build
