(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('data-point', {
            parent: 'entity',
            url: '/data-point',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DataPoints'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-point/data-points.html',
                    controller: 'DataPointController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('data-point-detail', {
            parent: 'data-point',
            url: '/data-point/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DataPoint'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/data-point/data-point-detail.html',
                    controller: 'DataPointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DataPoint', function($stateParams, DataPoint) {
                    return DataPoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'data-point',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('data-point-detail.edit', {
            parent: 'data-point-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-point/data-point-dialog.html',
                    controller: 'DataPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DataPoint', function(DataPoint) {
                            return DataPoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('data-point.new', {
            parent: 'data-point',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-point/data-point-dialog.html',
                    controller: 'DataPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                order: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('data-point', null, { reload: 'data-point' });
                }, function() {
                    $state.go('data-point');
                });
            }]
        })
        .state('data-point.edit', {
            parent: 'data-point',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-point/data-point-dialog.html',
                    controller: 'DataPointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DataPoint', function(DataPoint) {
                            return DataPoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-point', null, { reload: 'data-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('data-point.delete', {
            parent: 'data-point',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/data-point/data-point-delete-dialog.html',
                    controller: 'DataPointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DataPoint', function(DataPoint) {
                            return DataPoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('data-point', null, { reload: 'data-point' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
