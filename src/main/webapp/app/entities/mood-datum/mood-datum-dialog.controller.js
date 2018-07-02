(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('MoodDatumDialogController', MoodDatumDialogController);

    MoodDatumDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MoodDatum', 'User'];

    function MoodDatumDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MoodDatum, User) {
        var vm = this;

        vm.moodDatum = entity;
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
            if (vm.moodDatum.id !== null) {
                MoodDatum.update(vm.moodDatum, onSaveSuccess, onSaveError);
            } else {
                MoodDatum.save(vm.moodDatum, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:moodDatumUpdate', result);
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
