(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramHistoryDialogController', ProgramHistoryDialogController);

    ProgramHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProgramHistory', 'ProgramLevel', 'ProgramStep', 'UserInfo'];

    function ProgramHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProgramHistory, ProgramLevel, ProgramStep, UserInfo) {
        var vm = this;

        vm.programHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.programlevels = ProgramLevel.query();
        vm.programsteps = ProgramStep.query();
        vm.userinfos = UserInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.programHistory.id !== null) {
                ProgramHistory.update(vm.programHistory, onSaveSuccess, onSaveError);
            } else {
                ProgramHistory.save(vm.programHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:programHistoryUpdate', result);
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
