(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('SleepDatumDialogController', SleepDatumDialogController);

    SleepDatumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SleepDatum', 'User'];

    function SleepDatumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SleepDatum, User) {
        var vm = this;

        vm.sleepDatum = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sleepDatum.id !== null) {
                SleepDatum.update(vm.sleepDatum, onSaveSuccess, onSaveError);
            } else {
                SleepDatum.save(vm.sleepDatum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:sleepDatumUpdate', result);
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
