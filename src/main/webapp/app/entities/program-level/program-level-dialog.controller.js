(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramLevelDialogController', ProgramLevelDialogController);

    ProgramLevelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProgramLevel', 'ProgramStep', 'Program', 'ProgramHistory'];

    function ProgramLevelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProgramLevel, ProgramStep, Program, ProgramHistory) {
        var vm = this;

        vm.programLevel = entity;
        vm.clear = clear;
        vm.save = save;
        vm.programsteps = ProgramStep.query();
        vm.programs = Program.query();
        vm.programhistories = ProgramHistory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.programLevel.id !== null) {
                ProgramLevel.update(vm.programLevel, onSaveSuccess, onSaveError);
            } else {
                ProgramLevel.save(vm.programLevel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:programLevelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
