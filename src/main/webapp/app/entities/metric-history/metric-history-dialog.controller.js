(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MetricHistoryDialogController', MetricHistoryDialogController);

    MetricHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MetricHistory', 'TrackMetric', 'TrackMetricQuestion', 'UserInfo'];

    function MetricHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MetricHistory, TrackMetric, TrackMetricQuestion, UserInfo) {
        var vm = this;

        vm.metricHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.trackmetrics = TrackMetric.query({filter: 'metrichistory-is-null'});
        $q.all([vm.metricHistory.$promise, vm.trackmetrics.$promise]).then(function() {
            if (!vm.metricHistory.trackMetric || !vm.metricHistory.trackMetric.id) {
                return $q.reject();
            }
            return TrackMetric.get({id : vm.metricHistory.trackMetric.id}).$promise;
        }).then(function(trackMetric) {
            vm.trackmetrics.push(trackMetric);
        });
        vm.metricquestions = TrackMetricQuestion.query({filter: 'metrichistory-is-null'});
        $q.all([vm.metricHistory.$promise, vm.metricquestions.$promise]).then(function() {
            if (!vm.metricHistory.metricQuestion || !vm.metricHistory.metricQuestion.id) {
                return $q.reject();
            }
            return TrackMetricQuestion.get({id : vm.metricHistory.metricQuestion.id}).$promise;
        }).then(function(metricQuestion) {
            vm.metricquestions.push(metricQuestion);
        });
        vm.userinfos = UserInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.metricHistory.id !== null) {
                MetricHistory.update(vm.metricHistory, onSaveSuccess, onSaveError);
            } else {
                MetricHistory.save(vm.metricHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:metricHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
