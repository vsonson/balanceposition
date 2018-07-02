(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('FocusDatumDialogController', FocusDatumDialogController);

    FocusDatumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FocusDatum', 'User'];

    function FocusDatumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FocusDatum, User) {
        var vm = this;

        vm.focusDatum = entity;
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
            if (vm.focusDatum.id !== null) {
                FocusDatum.update(vm.focusDatum, onSaveSuccess, onSaveError);
            } else {
                FocusDatum.save(vm.focusDatum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:focusDatumUpdate', result);
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
