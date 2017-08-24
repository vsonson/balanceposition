(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NotifcationTriggerDialogController', NotifcationTriggerDialogController);

    NotifcationTriggerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotifcationTrigger', 'Trigger'];

    function NotifcationTriggerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotifcationTrigger, Trigger) {
        var vm = this;

        vm.notifcationTrigger = entity;
        vm.clear = clear;
        vm.save = save;
        vm.triggers = Trigger.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.notifcationTrigger.id !== null) {
                NotifcationTrigger.update(vm.notifcationTrigger, onSaveSuccess, onSaveError);
            } else {
                NotifcationTrigger.save(vm.notifcationTrigger, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:notifcationTriggerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
