(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('PathWayDeleteController',PathWayDeleteController);

    PathWayDeleteController.$inject = ['$uibModalInstance', 'entity', 'PathWay'];

    function PathWayDeleteController($uibModalInstance, entity, PathWay) {
        var vm = this;

        vm.pathWay = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PathWay.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
