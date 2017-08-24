(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveHistoryDialogController', IncentiveHistoryDialogController);

    IncentiveHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'IncentiveHistory', 'UserInfo'];

    function IncentiveHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, IncentiveHistory, UserInfo) {
        var vm = this;

        vm.incentiveHistory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.userinfos = UserInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.incentiveHistory.id !== null) {
                IncentiveHistory.update(vm.incentiveHistory, onSaveSuccess, onSaveError);
            } else {
                IncentiveHistory.save(vm.incentiveHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:incentiveHistoryUpdate', result);
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
