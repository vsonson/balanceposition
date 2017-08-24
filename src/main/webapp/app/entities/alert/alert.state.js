(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('alert', {
            parent: 'entity',
            url: '/alert?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Alerts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/alert/alerts.html',
                    controller: 'AlertController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('alert-detail', {
            parent: 'alert',
            url: '/alert/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Alert'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/alert/alert-detail.html',
                    controller: 'AlertDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Alert', function($stateParams, Alert) {
                    return Alert.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'alert',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('alert-detail.edit', {
            parent: 'alert-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/alert/alert-dialog.html',
                    controller: 'AlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Alert', function(Alert) {
                            return Alert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('alert.new', {
            parent: 'alert',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/alert/alert-dialog.html',
                    controller: 'AlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                alertType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('alert', null, { reload: 'alert' });
                }, function() {
                    $state.go('alert');
                });
            }]
        })
        .state('alert.edit', {
            parent: 'alert',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/alert/alert-dialog.html',
                    controller: 'AlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Alert', function(Alert) {
                            return Alert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('alert', null, { reload: 'alert' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('alert.delete', {
            parent: 'alert',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/alert/alert-delete-dialog.html',
                    controller: 'AlertDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Alert', function(Alert) {
                            return Alert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('alert', null, { reload: 'alert' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
