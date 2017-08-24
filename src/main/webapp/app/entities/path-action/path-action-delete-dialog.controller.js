(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathActionDeleteController',PathActionDeleteController);

    PathActionDeleteController.$inject = ['$uibModalInstance', 'entity', 'PathAction'];

    function PathActionDeleteController($uibModalInstance, entity, PathAction) {
        var vm = this;

        vm.pathAction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PathAction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
