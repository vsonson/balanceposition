(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MetricDatumDialogController', MetricDatumDialogController);

    MetricDatumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MetricDatum', 'DataPoint', 'User'];

    function MetricDatumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MetricDatum, DataPoint, User) {
        var vm = this;

        vm.metricDatum = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.datapoints = DataPoint.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.metricDatum.id !== null) {
                MetricDatum.update(vm.metricDatum, onSaveSuccess, onSaveError);
            } else {
                MetricDatum.save(vm.metricDatum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:metricDatumUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.timestamp = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
