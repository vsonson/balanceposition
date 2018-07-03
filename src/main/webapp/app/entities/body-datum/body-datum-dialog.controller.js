(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('BodyDatumDialogController', BodyDatumDialogController);

    BodyDatumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BodyDatum', 'User'];

    function BodyDatumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BodyDatum, User) {
        var vm = this;

        vm.bodyDatum = entity;
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
            if (vm.bodyDatum.id !== null) {
                BodyDatum.update(vm.bodyDatum, onSaveSuccess, onSaveError);
            } else {
                BodyDatum.save(vm.bodyDatum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:bodyDatumUpdate', result);
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
