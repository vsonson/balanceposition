(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveActionDialogController', IncentiveActionDialogController);

    IncentiveActionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'IncentiveAction', 'Trigger', 'Incentive'];

    function IncentiveActionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, IncentiveAction, Trigger, Incentive) {
        var vm = this;

        vm.incentiveAction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.triggers = Trigger.query();
        vm.incentives = Incentive.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.incentiveAction.id !== null) {
                IncentiveAction.update(vm.incentiveAction, onSaveSuccess, onSaveError);
            } else {
                IncentiveAction.save(vm.incentiveAction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:incentiveActionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
