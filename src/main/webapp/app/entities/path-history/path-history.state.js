(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('path-history', {
            parent: 'entity',
            url: '/path-history?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-history/path-histories.html',
                    controller: 'PathHistoryController',
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
        .state('path-history-detail', {
            parent: 'path-history',
            url: '/path-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-history/path-history-detail.html',
                    controller: 'PathHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PathHistory', function($stateParams, PathHistory) {
                    return PathHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'path-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('path-history-detail.edit', {
            parent: 'path-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-history/path-history-dialog.html',
                    controller: 'PathHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathHistory', function(PathHistory) {
                            return PathHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-history.new', {
            parent: 'path-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-history/path-history-dialog.html',
                    controller: 'PathHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                isCompleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('path-history', null, { reload: 'path-history' });
                }, function() {
                    $state.go('path-history');
                });
            }]
        })
        .state('path-history.edit', {
            parent: 'path-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-history/path-history-dialog.html',
                    controller: 'PathHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathHistory', function(PathHistory) {
                            return PathHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-history', null, { reload: 'path-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-history.delete', {
            parent: 'path-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-history/path-history-delete-dialog.html',
                    controller: 'PathHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PathHistory', function(PathHistory) {
                            return PathHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-history', null, { reload: 'path-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
