SUMMARY = "Open Programmable Acceleration Engine"
HOMEPAGE = "https://github.com/OFS/opae-sdk"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=5351f05d1aa340cb91bb885c2fd82fc7"
SRC_URI = "git://github.com/OFS/opae-sdk;protocol=https;branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

DEPENDS = "\
    cli11 \
    hwloc \
    json-c \
    libedit \
    python3 \
    python3-jsonschema-native \
    python3-pybind11 \
    python3-pyyaml-native \
    python3-setuptools-native \
    spdlog \
    tbb \
    udev \
"

# Set package version from latest git tag and abbreviated commit hash
inherit gitpkgv
PKGV = "${GITPKGVTAG}"

inherit cmake pkgconfig python3native

# Work around linking error for Python extension modules
#
# ld: cannot find crti.o: No such file or directory
# ld: cannot find crtbeginS.o: No such file or directory
# ld: cannot find -lstdc++: No such file or directory
# ld: cannot find -lm: No such file or directory
# ld: cannot find -lgcc_s: No such file or directory
# ld: cannot find -lpthread: No such file or directory
# ld: cannot find -lc: No such file or directory
# ld: cannot find -lgcc_s: No such file or directory
# ld: cannot find crtendS.o: No such file or directory
# ld: cannot find crtn.o: No such file or directory
#
LDFLAGS += "--sysroot=${STAGING_DIR_TARGET}"

# Work around setuptools overwriting hashbang with build path
# https://cgit.openembedded.org/openembedded-core/tree/meta/classes-recipe/setuptools3_legacy.bbclass?id=8e9ec03c73e8c09e223d6f6cce297df363991350
do_install:append() {
    sed -i '1s:^#!${PYTHON}$:#!${bindir}/python3:' ${D}${bindir}/*
}

FILES:${PN}+= "${prefix}/*"

RDEPENDS:${PN} = "\
    bash \
    python3-core \
    python3-jsonschema \
    python3-pyyaml\
"