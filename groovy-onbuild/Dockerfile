# Copyright 2014 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM java:8
MAINTAINER Ray Tsang <saturnism@gmail.com>

ENV SRC_DIR /app/src
ENV BIN_DIR /app/bin
ENV GROOVY_VERSION 2.4.3
ENV SPRINGBOOT_VERSION 1.2.3.RELEASE

RUN curl -s get.gvmtool.net | bash
RUN bash -c "source $HOME/.gvm/bin/gvm-init.sh && gvm install groovy $GROOVY_VERSION && gvm install springboot $SPRINGBOOT_VERSION"

RUN mkdir -p $SRC_DIR
WORKDIR $SRC_DIR

EXPOSE 8080

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/bin/app.jar"]

ONBUILD RUN mkdir -p $BIN_DIR
ONBUILD ADD . $SRC_DIR
ONBUILD RUN bash -c "source $HOME/.bashrc && spring jar $BIN_DIR/app.jar $SRC_DIR"
