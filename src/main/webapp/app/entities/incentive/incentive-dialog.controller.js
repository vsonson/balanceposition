(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('IncentiveDialogController', IncentiveDialogController);

    IncentiveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Incentive', 'IncentiveAction'];

    function IncentiveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Incentive, IncentiveAction) {
        var vm = this;

        vm.incentive = entity;
        vm.clear = clear;
        vm.save = save;
        vm.incentiveactions = IncentiveAction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.incentive.id !== null) {
                Incentive.update(vm.incentive, onSaveSuccess, onSaveError);
            } else {
                Incentive.save(vm.incentive, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:incentiveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
