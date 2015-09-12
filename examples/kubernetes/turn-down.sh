#!/bin/bash
###############################################################################
# Copyright 2015 Google Inc. All rights reserved.
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
###############################################################################

kubectl delete services helloworldui

#kubectl scale --replicas=0 replicationcontrollers helloworldui-controller-latest
kubectl delete replicationcontrollers helloworldui-controller-v1

#kubectl scale --replicas=0 replicationcontrollers helloworldui-controller-latest
kubectl delete replicationcontrollers helloworldui-controller-v2

#kubectl scale --replicas=0 replicationcontrollers helloworldui-controller-latest
kubectl delete replicationcontrollers helloworldui-controller-latest

kubectl delete services helloworldservice

#kubectl scale --replicas=0 replicationcontrollers helloworldservice-controller-latest
kubectl delete replicationcontrollers helloworldservice-controller-latest

#kubectl scale --replicas=0 replicationcontrollers helloworldservice-controller-v1
kubectl delete replicationcontrollers helloworldservice-controller-v1

#kubectl scale --replicas=0 replicationcontrollers helloworldservice-controller-v2
kubectl delete replicationcontrollers helloworldservice-controller-v2

kubectl delete services redis
kubectl delete pods redis

kubectl delete services guestbookservice
kubectl delete replicationcontrollers guestbookservice-controller-latest

kubectl delete services mysql
kubectl delete pods mysql
