(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('StressDatumDialogController', StressDatumDialogController);

    StressDatumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StressDatum', 'User'];

    function StressDatumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StressDatum, User) {
        var vm = this;

        vm.stressDatum = entity;
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
            if (vm.stressDatum.id !== null) {
                StressDatum.update(vm.stressDatum, onSaveSuccess, onSaveError);
            } else {
                StressDatum.save(vm.stressDatum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:stressDatumUpdate', result);
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
