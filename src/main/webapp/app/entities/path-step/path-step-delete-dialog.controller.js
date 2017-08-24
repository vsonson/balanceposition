(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathStepDeleteController',PathStepDeleteController);

    PathStepDeleteController.$inject = ['$uibModalInstance', 'entity', 'PathStep'];

    function PathStepDeleteController($uibModalInstance, entity, PathStep) {
        var vm = this;

        vm.pathStep = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PathStep.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
