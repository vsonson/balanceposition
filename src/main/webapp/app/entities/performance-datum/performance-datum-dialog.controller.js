(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PerformanceDatumDialogController', PerformanceDatumDialogController);

    PerformanceDatumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PerformanceDatum', 'User'];

    function PerformanceDatumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PerformanceDatum, User) {
        var vm = this;

        vm.performanceDatum = entity;
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
            if (vm.performanceDatum.id !== null) {
                PerformanceDatum.update(vm.performanceDatum, onSaveSuccess, onSaveError);
            } else {
                PerformanceDatum.save(vm.performanceDatum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:performanceDatumUpdate', result);
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
