"""Prints the name of every failed test found in the given JUnit XML results directory, one per line."""
import sys
import xml.etree.ElementTree as ET
from pathlib import Path


def main(results_dir: str) -> None:
    for report in Path(results_dir).glob("*.xml"):
        for case in ET.parse(report).getroot().iter("testcase"):
            if case.find("failure") is not None or case.find("error") is not None:
                print(f"{case.get('classname')} > {case.get('name')}")


if __name__ == "__main__":
    main(sys.argv[1])
