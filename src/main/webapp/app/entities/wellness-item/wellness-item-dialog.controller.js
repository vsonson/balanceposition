(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('WellnessItemDialogController', WellnessItemDialogController);

    WellnessItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WellnessItem', 'Trigger'];

    function WellnessItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WellnessItem, Trigger) {
        var vm = this;

        vm.wellnessItem = entity;
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
            if (vm.wellnessItem.id !== null) {
                WellnessItem.update(vm.wellnessItem, onSaveSuccess, onSaveError);
            } else {
                WellnessItem.save(vm.wellnessItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:wellnessItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
