(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramStepDialogController', ProgramStepDialogController);

    ProgramStepDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProgramStep', 'ProgramLevel', 'ProgramHistory'];

    function ProgramStepDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProgramStep, ProgramLevel, ProgramHistory) {
        var vm = this;

        vm.programStep = entity;
        vm.clear = clear;
        vm.save = save;
        vm.programlevels = ProgramLevel.query();
        vm.programhistories = ProgramHistory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.programStep.id !== null) {
                ProgramStep.update(vm.programStep, onSaveSuccess, onSaveError);
            } else {
                ProgramStep.save(vm.programStep, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:programStepUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
