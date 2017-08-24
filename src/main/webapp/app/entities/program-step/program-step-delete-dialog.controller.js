(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramStepDeleteController',ProgramStepDeleteController);

    ProgramStepDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProgramStep'];

    function ProgramStepDeleteController($uibModalInstance, entity, ProgramStep) {
        var vm = this;

        vm.programStep = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProgramStep.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
