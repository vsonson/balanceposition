(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TriggerDialogController', TriggerDialogController);

    TriggerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Trigger', 'NotifcationTrigger', 'WellnessItem', 'IncentiveAction'];

    function TriggerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Trigger, NotifcationTrigger, WellnessItem, IncentiveAction) {
        var vm = this;

        vm.trigger = entity;
        vm.clear = clear;
        vm.save = save;
        vm.notifcationtriggers = NotifcationTrigger.query();
        vm.wellnessitems = WellnessItem.query();
        vm.incentiveactions = IncentiveAction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.trigger.id !== null) {
                Trigger.update(vm.trigger, onSaveSuccess, onSaveError);
            } else {
                Trigger.save(vm.trigger, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:triggerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
