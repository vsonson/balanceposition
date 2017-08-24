(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathStepDialogController', PathStepDialogController);

    PathStepDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PathStep', 'PathWay', 'PathAction'];

    function PathStepDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PathStep, PathWay, PathAction) {
        var vm = this;

        vm.pathStep = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pathways = PathWay.query();
        vm.pathactions = PathAction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pathStep.id !== null) {
                PathStep.update(vm.pathStep, onSaveSuccess, onSaveError);
            } else {
                PathStep.save(vm.pathStep, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:pathStepUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
