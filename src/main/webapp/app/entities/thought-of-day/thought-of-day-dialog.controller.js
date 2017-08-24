(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ThoughtOfDayDialogController', ThoughtOfDayDialogController);

    ThoughtOfDayDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ThoughtOfDay'];

    function ThoughtOfDayDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ThoughtOfDay) {
        var vm = this;

        vm.thoughtOfDay = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.thoughtOfDay.id !== null) {
                ThoughtOfDay.update(vm.thoughtOfDay, onSaveSuccess, onSaveError);
            } else {
                ThoughtOfDay.save(vm.thoughtOfDay, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:thoughtOfDayUpdate', result);
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
